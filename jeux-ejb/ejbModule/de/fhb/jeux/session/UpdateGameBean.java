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

    protected boolean updateSets(IGame game, GameDTO gameDTO) {
        int updatedSets = 0;

        List<ShowdownGameSet> existingSets = game.getSets();
        gameDTO = alignPlayers(game, gameDTO);
        List<GameSetDTO> newSets = gameDTO.getSets();

        if (existingSets.size() == newSets.size()) {

            ListIterator<ShowdownGameSet> setsIterator = existingSets.listIterator();
            while (setsIterator.hasNext()) {
                IGameSet oldSet = setsIterator.next();
                IPlayer player1 = oldSet.getGame().getPlayer1();
                IPlayer player2 = oldSet.getGame().getPlayer2();
                // DTOs get counted and index-accessed in parallel
                GameSetDTO newSet = newSets.get(updatedSets++);

                // copy scores
                oldSet.setPlayer1Score(newSet.getPlayer1Score());
                oldSet.setPlayer2Score(newSet.getPlayer2Score());

                // write set winner
                if (oldSet.getPlayer1Score() > 0 || oldSet.getPlayer2Score() > 0) {

                    if (oldSet.getPlayer1Score() > oldSet.getPlayer2Score()) {
                        oldSet.setWinner(player1);

                        logger.info("Set winner: " + player1 + ", "
                                + oldSet.getPlayer1Score() + ":"
                                + oldSet.getPlayer2Score());
                    } else if (oldSet.getPlayer2Score() > oldSet.getPlayer1Score()) {
                        oldSet.setWinner(player2);

                        logger.info("Set winner: " + player2 + ", "
                                + oldSet.getPlayer2Score() + ":"
                                + oldSet.getPlayer1Score());
                    }
                }
            }
            game.setSets(existingSets);
        }

        return updatedSets > 0;
    }

    @Override
    public boolean updateGame(GameDTO gameDTO, BonusPointsDistribution config) {
        boolean success = false;
        boolean updated;

        if (gameDTO != null) {
            IGame game = gameDAO.getGameById(gameDTO.getId());
            if (game != null) {
                if (game.hasWinner()) {
                    game = clearGame(game, config);
                }

                updated = updateSets(game, gameDTO);

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
                            // TODO Game.clearSets()
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
                        game = finishGame(game, config);
                    }

                    // write back
                    gameDAO.updateGame(game);

                    IGroup group = game.getGroup();
                    // set group = completed if this was its last game
                    if (gameDAO.getUnplayedGamesInGroup(group).isEmpty()) {
                        group.setCompleted();
                        // DO NOT setActive(false) here,
                        // group stays active until round switched!
                    }
                    success = true;
                }

            }
        }

        return success;
    }

    protected IGame finishGame(IGame game, BonusPointsDistribution config) {
        updateScoreRatios(game, true);
        addScores(game);
        game.setPlayedAt(new Date());

        IPlayer winner;
        IPlayer loser;
        int setsWonByPlayer1 = game.getSetsWonByPlayer1();
        int setsWonByPlayer2 = game.getSetsWonByPlayer2();
        int setsWonByWinner;
        int setsWonByLoser;
        int setsPlayed = game.getSetsPlayed();

        if (setsWonByPlayer1 == setsWonByPlayer2) {
            return game;
        } else {
            if (setsWonByPlayer1 > setsWonByPlayer2) {
                winner = game.getPlayer1();
                loser = game.getPlayer2();
                setsWonByWinner = game.getSetsWonByPlayer1();
                setsWonByLoser = game.getSetsWonByPlayer2();
            } else {
                winner = game.getPlayer2();
                loser = game.getPlayer1();
                setsWonByWinner = game.getSetsWonByPlayer2();
                setsWonByLoser = game.getSetsWonByPlayer1();
            }
        }

        game.setWinner(winner);
        winner.addWonGame();
        playerDAO.updatePlayer(winner);

        logger.info("Game winner: " + winner + ", "
                + setsWonByWinner + ":" + setsWonByLoser);

        // add bonus points (only) if more than one set has
        // been played
        if (game.getSets().size() > 1) {
            addBonusPoints(new BonusPointsDistributor(config).getBonusPoints(
                    setsPlayed, setsWonByWinner), winner, loser);
        }

        return game;
    }

    // switch players if necessary (player1Score becomes player2Score etc.)
// needed in rare case where client JSON might be mixed up
    private GameDTO alignPlayers(IGame game, GameDTO dto) {

        if (game.getPlayer1().getId() == dto.getPlayer1Id()
                && game.getPlayer2().getId() == dto.getPlayer2Id()) {
            return dto;
        }

        GameDTO alignedDTO = new GameDTO(dto);
        // vice versa
        alignedDTO.setPlayer1Id(dto.getPlayer2Id());
        alignedDTO.setPlayer1Name(dto.getPlayer2Name());
        alignedDTO.setPlayer2Id(dto.getPlayer1Id());
        alignedDTO.setPlayer2Name(dto.getPlayer1Name());
        // align set list
        ArrayList<GameSetDTO> alignedSets = new ArrayList<>();
        for (GameSetDTO set : dto.getSets()) {
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

    protected IGame clearGame(IGame game, BonusPointsDistribution config) {
        BonusPointsDistributor bonusPointsDistributor = new BonusPointsDistributor(config);
        logger.debug("Before (already played): " + game);
		// game has already been played, so user wants to edit it
        // that means we have to (in this order!):

        // - subtract 1 won game for the winner
        game.getWinner().subtractWonGame();

        // - subtract/add points ratio for existing game result
        updateScoreRatios(game, false);

        // - clear any bonus points awarded before for this game
        subtractBonusPoints(
                bonusPointsDistributor.getBonusPoints(
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

        return game;

    }

    private void addBonusPoints(HashMap<String, Integer> bonusPoints,
            IPlayer winner, IPlayer loser) {
        winner.setPoints(winner.getPoints() + bonusPoints.get("winner"));
        loser.setPoints(loser.getPoints() + bonusPoints.get("loser"));

        logger.info("Bonus points: " + winner + " +"
                + bonusPoints.get("winner") + " = " + winner.getPoints() + ", "
                + loser + " +" + bonusPoints.get("loser") + " = "
                + loser.getPoints());
    }

    private void subtractBonusPoints(HashMap<String, Integer> bonusPoints,
            IPlayer winner, IPlayer loser) {
        winner.setPoints(winner.getPoints() - bonusPoints.get("winner"));
        loser.setPoints(loser.getPoints() - bonusPoints.get("loser"));

        logger.info("Bonus points: " + winner + " -"
                + bonusPoints.get("winner") + " = " + winner.getPoints() + ", "
                + loser + " -" + bonusPoints.get("loser") + " = "
                + loser.getPoints());
    }

    protected int getScore(IGame game, IPlayer player) {
        int score = 0;
        IPlayer player1 = game.getPlayer1();
        IPlayer player2 = game.getPlayer2();

        if (player != null) {
            if (player.equals(player1)) {
                for (ShowdownGameSet set : game.getSets()) {
                    score += set.getPlayer1Score();
                }
            } else if (player.equals(player2)) {
                for (ShowdownGameSet set : game.getSets()) {
                    score += set.getPlayer2Score();
                }
            }
        }

        return score;
    }

    protected void addScores(IGame game) {
        for (IPlayer player : game.getPlayers()) {
            player.setScore(player.getScore() + getScore(game, player));
        }
    }

    protected void subtractScores(IGame game) {
        for (IPlayer player : game.getPlayers()) {
            player.setScore(player.getScore() - getScore(game, player));
        }
    }

    // NB: the winner is not necessarily the one who has scored more!
    private void updateScoreRatios(IGame game, boolean addToTotal) {
        int player1Score = getScore(game, game.getPlayer1());
        int player2Score = getScore(game, game.getPlayer2());
        int difference = Math.abs(player1Score - player2Score);

        IPlayer player1 = game.getPlayer1();
        IPlayer player2 = game.getPlayer2();

        logger.info(player1 + " has scored " + player1Score + " in total");
        logger.info(player2 + " has scored " + player2Score + " in total");

        if (player1Score > player2Score) {

            if (addToTotal) {
                player1.setScoreRatio(player1.getScoreRatio() + difference);
                player2.setScoreRatio(player2.getScoreRatio() - difference);
                logger.info(player1 + " +" + difference + " = "
                        + player1.getScoreRatio());
                logger.info(player2 + " -" + difference + " = "
                        + player2.getScoreRatio());
            } else {
                logger.info("Resetting scores");
                player2.setScoreRatio(player2.getScoreRatio() + difference);
                player1.setScoreRatio(player1.getScoreRatio() - difference);

                logger.info(player2 + " +" + difference + " = "
                        + player2.getScoreRatio());
                logger.info(player1 + " -" + difference + " = "
                        + player1.getScoreRatio());
            }

        } else if (player2Score > player1Score) {
            if (addToTotal) {
                player2.setScoreRatio(player2.getScoreRatio() + difference);
                player1.setScoreRatio(player1.getScoreRatio() - difference);

                logger.info(player2 + " +" + difference + " = "
                        + player2.getScoreRatio());
                logger.info(player1 + " -" + difference + " = "
                        + player1.getScoreRatio());
            } else {
                logger.info("Resetting scores");
                player1.setScoreRatio(player1.getScoreRatio() + difference);
                player2.setScoreRatio(player2.getScoreRatio() - difference);

                logger.info(player1 + " +" + difference + " = "
                        + player1.getScoreRatio());
                logger.info(player2 + " -" + difference + " = "
                        + player2.getScoreRatio());
            }
        }
    }
}
