package de.fhb.jeux.session;

import de.fhb.jeux.config.BonusPointsDistribution;
import de.fhb.jeux.config.BonusPointsDistributor;
import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.dto.GameSetDTO;
import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGameSet;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownGameSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.logging.Logger;

@Stateless

public class UpdateGameBean implements UpdateGameRemote, UpdateGameLocal {

    protected static Logger logger = Logger.getLogger(UpdateGameBean.class);

    @EJB
    private GameDAO gameDAO;

    @EJB
    private PlayerDAO playerDAO;

    @EJB
    private GroupDAO groupDAO;

    @EJB
    private PlayerLocal playerBean;

    @EJB
    private RoundSwitchLocal roundSwitchBean;

    public UpdateGameBean() {
    }

    @Override
    public boolean updateGame(GameDTO gameDTO, BonusPointsDistribution config) {
        boolean success = false;
        boolean updated = false;
        int numDTOs = 0;

        if (gameDTO != null) {
            IGame game = gameDAO.getGameById(gameDTO.getId());

            if (game != null) {

                if (game.hasWinner()) {
                    clearGame(game, config);
                }

                List<ShowdownGameSet> existingSets = game.getSets();
                List<GameSetDTO> newSets = gameDTO.getSets();

                // same amount of overall sets required
                if (existingSets.size() == newSets.size()) {

                    IPlayer player1 = game.getPlayer1();
                    IPlayer player2 = game.getPlayer2();

                    // copy scores from DTO to Entity
                    // and try to determine set winner
                    // pre-align if input is vice versa to save code (DRY)
                    if (player1.getId() == gameDTO.getPlayer2Id()
                            && player2.getId() == gameDTO.getPlayer1Id()) {
                        gameDTO = alignPlayers(gameDTO);
                    }

                    // scores aligned (now)?
                    if (player1.getId() == gameDTO.getPlayer1Id()
                            && player2.getId() == gameDTO.getPlayer2Id()) {

                        ListIterator<ShowdownGameSet> setsIterator = existingSets
                                .listIterator();

                        while (setsIterator.hasNext()) {
                            IGameSet oldSet = setsIterator.next();
                            String oldSetGroupName = oldSet.getGame().getGroup().getName();
                            // DTOs get counted and index-accessed in parallel
                            GameSetDTO newSet = newSets.get(numDTOs++);

                            // copy scores
                            oldSet.setPlayer1Score(newSet.getPlayer1Score());
                            oldSet.setPlayer2Score(newSet.getPlayer2Score());

                            // write set winner
                            if (oldSet.getPlayer1Score() > 0
                                    || oldSet.getPlayer2Score() > 0) {

                                if (oldSet.getPlayer1Score() > oldSet
                                        .getPlayer2Score()) {
                                    oldSet.setWinner(oldSet.getGame().getPlayer1());

                                    logger.info("'" + oldSetGroupName + "' Set winner: "
                                            + oldSet.getGame().getPlayer1()
                                            .getName() + ", "
                                            + oldSet.getPlayer1Score() + ":"
                                            + oldSet.getPlayer2Score());
                                } else if (oldSet.getPlayer2Score() > oldSet
                                        .getPlayer1Score()) {
                                    oldSet.setWinner(oldSet.getGame().getPlayer2());

                                    logger.info("'" + oldSetGroupName + "' Set winner: "
                                            + oldSet.getGame().getPlayer2()
                                            .getName() + ", "
                                            + oldSet.getPlayer2Score() + ":"
                                            + oldSet.getPlayer1Score());
                                }
                            }
                        }

                        if (numDTOs > 0) {
                            updated = true;
                            // write back
                            game.setSets(existingSets);
                        }

                    } else {
                        logger.warn("Player ID mismatch");
                    }

                    // sets updated, let's see if game over
                    if (updated) {
                        int setsPlayed = game.getSetsPlayed();
                        int setsWonByPlayer1 = game.getSetsWonByPlayer1();
                        int setsWonByPlayer2 = game.getSetsWonByPlayer2();
                        boolean gameOver = false;
                        int minSets = game.getGroup().getMinSets();
                        int maxSets = game.getGroup().getMaxSets();

                        // TODO make the following transactional #app-design
                        // we have a game winner if
                        // - all sets have been played OR enough sets have been
                        // played
                        // - one player has won more sets than the other
                        // - no one player has played more than min amount of sets
                        // we'll also calculate and write back
                        // - (bonus) points for won sets
                        // - score ratios for opponents
                        // (only) one of the players has won minimum required sets
                        if (setsPlayed == maxSets) {
                            if (maxSets == 1
                                    || maxSets > setsWonByPlayer1
                                    && maxSets > setsWonByPlayer2) {
                                gameOver = true;
                            } else {
                                logger.error("One player has won one set too much (max sets "
                                        + maxSets + "), sets won by players: "
                                        + setsWonByPlayer1 + ", "
                                        + setsWonByPlayer2);
                                // - clear sets
                                for (ShowdownGameSet set : game.getSets()) {
                                    set.setPlayer1Score(0);
                                    set.setPlayer2Score(0);
                                    set.setWinner(null);
                                }
                            }
                        } else if (setsWonByPlayer1 == minSets && setsWonByPlayer2 < minSets) {
                            gameOver = true;
                        } else if (setsWonByPlayer2 == minSets && setsWonByPlayer1 < minSets) {
                            gameOver = true;
                        } else {
                            logger.error("Unable to determine winner for sets in game");
                        }

                        if (gameOver) {
                            logger.info("Game over");
                            // write score ratios already (no need to know the
                            // winner yet)
                            updateScoreRatios(game, true);

                            game.setPlayedAt(new Date());

                            if (setsWonByPlayer1 > setsWonByPlayer2) {

                                // set player 1 as winner
                                game.setWinner(player1);
                                player1.addWonGame();
                                playerDAO.updatePlayer(player1);

                                logger.info("Game winner: " + player1.getName()
                                        + ", " + setsWonByPlayer1 + ":"
                                        + setsWonByPlayer2);

                                // add bonus points (only) if more than one set has
                                // been played
                                if (game.getSets().size() > 1) {

                                    addBonusPoints(
                                            BonusPointsDistributor.getBonusPoints(
                                                    config, setsPlayed,
                                                    setsWonByPlayer1), player1,
                                            player2);
                                }

                            } else if (setsWonByPlayer2 > setsWonByPlayer1) {

                                // set player 2 as winner
                                game.setWinner(player2);
                                player2.addWonGame();
                                playerDAO.updatePlayer(player2);

                                logger.info("Game winner: " + player2.getName()
                                        + ", " + setsWonByPlayer2 + ":"
                                        + setsWonByPlayer1);

                                // add bonus points (only) if more than one set has
                                // been played
                                if (game.getSets().size() > 1) {
                                    addBonusPoints(
                                            BonusPointsDistributor.getBonusPoints(
                                                    config, setsPlayed,
                                                    setsWonByPlayer2), player2,
                                            player1);
                                }
                            }
                        }

                        // write back
                        gameDAO.updateGame(game);

                        IGroup group = game.getGroup();
                        // set group = completed if this was its last game
                        if (gameDAO.getUnplayedGamesInGroup(group).isEmpty()) {
                            group.setCompleted();
                            // DO NOT setActive(false) here,
                            // group stays active until round switched!
                            success = true;
                        } else {
                            // !
                            success = true;
                        }
                    }
                }
            }
        }

        return success;
    }

    // switch players if necessary (player1Score becomes player2Score etc.)
    // needed in rare case where client JSON might be mixed up
    private GameDTO alignPlayers(GameDTO unalignedDTO) {
        // log level info because unusual
        logger.debug("Unaligned DTO: " + unalignedDTO);

        GameDTO alignedDTO = new GameDTO(unalignedDTO);
        // vice versa
        alignedDTO.setPlayer1Id(unalignedDTO.getPlayer2Id());
        alignedDTO.setPlayer1Name(unalignedDTO.getPlayer2Name());
        alignedDTO.setPlayer2Id(unalignedDTO.getPlayer1Id());
        alignedDTO.setPlayer2Name(unalignedDTO.getPlayer1Name());
        // align set list
        ArrayList<GameSetDTO> alignedSets = new ArrayList<>();
        for (GameSetDTO set : unalignedDTO.getSets()) {
            logger.debug("Unaligned set: " + set);
            // vice versa
            // we have to set any winner to null
            GameSetDTO alignedSet = new GameSetDTO(set.getId(),
                    set.getGameId(), 0, set.getPlayer2Score(),
                    set.getPlayer1Score(), null, set.getNumber());
            logger.debug("Aligned set: " + alignedSet);

            alignedSets.add(alignedSet);
        }

        alignedDTO.setSets(alignedSets);
        logger.debug("Aligned DTO: " + alignedDTO);

        return alignedDTO;
    }

    private void clearGame(IGame game, BonusPointsDistribution config) {
        logger.debug("Before (already played): " + game);
		// game has already been played, so user wants to edit it
        // that means we have to (in this order!):

        // - subtract 1 won game for the winner
        game.getWinner().subtractWonGame();

        // - subtract/add points ratio for existing game result
        updateScoreRatios(game, false);

        // - clear any bonus points awarded before for this game
        subtractBonusPoints(
                BonusPointsDistributor.getBonusPoints(config,
                        game.getSetsPlayed(), game.getSetsPlayedByWinner()),
                game.getWinner(), game.getLoser());

        // - clear sets
        for (ShowdownGameSet set : game.getSets()) {
            set.setPlayer1Score(0);
            set.setPlayer2Score(0);
            set.setWinner(null);
        }

        // - clear the winner
        game.setWinner(null);

    }

    private void addBonusPoints(HashMap<String, Integer> bonusPoints,
            IPlayer winner, IPlayer loser) {
        winner.setPoints(winner.getPoints() + bonusPoints.get("winner"));
        loser.setPoints(loser.getPoints() + bonusPoints.get("loser"));
        String groupName = winner.getGroup().getName();

        logger.info("'" + groupName + "' Bonus points: " + winner.getName() + " +"
                + bonusPoints.get("winner") + " = " + winner.getPoints() + ", "
                + loser.getName() + " +" + bonusPoints.get("loser") + " = "
                + loser.getPoints());
    }

    private void subtractBonusPoints(HashMap<String, Integer> bonusPoints,
            IPlayer winner, IPlayer loser) {
        winner.setPoints(winner.getPoints() - bonusPoints.get("winner"));
        loser.setPoints(loser.getPoints() - bonusPoints.get("loser"));
        String groupName = winner.getGroup().getName();

        logger.info("'" + groupName + "' Bonus points: " + winner.getName() + " -"
                + bonusPoints.get("winner") + " = " + winner.getPoints() + ", "
                + loser.getName() + " -" + bonusPoints.get("loser") + " = "
                + loser.getPoints());
    }

    // NB: the winner is not necessarily the one who has scored more!
    private void updateScoreRatios(IGame game, boolean addToTotal) {
        int resultPlayer1Score = 0;
        int resultPlayer2Score = 0;
        int difference = 0;

        IPlayer player1 = game.getPlayer1();
        IPlayer player2 = game.getPlayer2();
        String groupName = game.getGroup().getName();

        for (ShowdownGameSet set : game.getSets()) {
            resultPlayer1Score += set.getPlayer1Score();
            resultPlayer2Score += set.getPlayer2Score();
        }

        logger.info("'" + groupName + "' " + player1.getName() + " has scored " + resultPlayer1Score
                + " in total");
        logger.info("'" + groupName + "' " + player2.getName() + " has scored " + resultPlayer2Score
                + " in total");

        difference = Math.abs(resultPlayer1Score - resultPlayer2Score);

        if (resultPlayer1Score > resultPlayer2Score) {

            if (addToTotal) {
                player1.setScoreRatio(player1.getScoreRatio() + difference);
                player2.setScoreRatio(player2.getScoreRatio() - difference);
                logger.info("'" + groupName + "' " + player1.getName() + " +" + difference + " = "
                        + player1.getScoreRatio());
                logger.info("'" + groupName + "' " + player2.getName() + " -" + difference + " = "
                        + player2.getScoreRatio());
            } else {
                logger.debug("Resetting scores");
                player2.setScoreRatio(player2.getScoreRatio() + difference);
                player1.setScoreRatio(player1.getScoreRatio() - difference);

                logger.info("'" + groupName + "' " + player2.getName() + " +" + difference + " = "
                        + player2.getScoreRatio());
                logger.info("'" + groupName + "' " + player1.getName() + " -" + difference + " = "
                        + player1.getScoreRatio());
            }

        } else if (resultPlayer2Score > resultPlayer1Score) {
            if (addToTotal) {
                player2.setScoreRatio(player2.getScoreRatio() + difference);
                player1.setScoreRatio(player1.getScoreRatio() - difference);

                logger.info("'" + groupName + "' " + player2.getName() + " +" + difference + " = "
                        + player2.getScoreRatio());
                logger.info("'" + groupName + "' " + player1.getName() + " -" + difference + " = "
                        + player1.getScoreRatio());
            } else {
                logger.debug("Resetting scores");
                player1.setScoreRatio(player1.getScoreRatio() + difference);
                player2.setScoreRatio(player2.getScoreRatio() - difference);
                logger.info("'" + groupName + "' " + player1.getName() + " +" + difference + " = "
                        + player1.getScoreRatio());
                logger.info("'" + groupName + "' " + player2.getName() + " -" + difference + " = "
                        + player2.getScoreRatio());
            }
        } else {
            // if scores are identical, nothing to do?
        }
    }
}
