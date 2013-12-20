package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.config.BonusPointsDistributor;
import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.dto.GameSetDTO;
import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGameSet;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownGameSet;

@Stateless
public class UpdateGameBean implements UpdateGameRemote, UpdateGameLocal {

	protected static Logger logger = Logger.getLogger(UpdateGameBean.class);

	@EJB
	private GameDAO gameDAO;

	@EJB
	private PlayerLocal playerBean;

	public UpdateGameBean() {
	}

	@Override
	public boolean updateGame(GameDTO gameDTO, String bonusPointsConfigPath) {
		boolean success = false;
		boolean updated = false;
		int numDTOs = 0;

		IGame game = gameDAO.getGameById(gameDTO.getId());

		if (game != null && gameDTO != null) {

			logger.debug("Before: " + game);

			List<ShowdownGameSet> existingSets = game.getSets();
			List<GameSetDTO> newSets = gameDTO.getSets();

			// same amount of sets required
			if (existingSets.size() == newSets.size()) {

				// copy scores from DTO to Entity
				// and try to determine set winner

				// pre-align if input is vice versa to save code (DRY)

				if (game.getPlayer1().getId() == gameDTO.getPlayer2Id()
						&& game.getPlayer2().getId() == gameDTO.getPlayer1Id()) {
					gameDTO = alignPlayers(gameDTO);
				}

				// scores aligned?
				if (game.getPlayer1().getId() == gameDTO.getPlayer1Id()
						&& game.getPlayer2().getId() == gameDTO.getPlayer2Id()) {

					ListIterator<ShowdownGameSet> setsIterator = existingSets
							.listIterator();

					while (setsIterator.hasNext()) {
						IGameSet oldSet = setsIterator.next();
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

								logger.debug("Set winner: "
										+ oldSet.getGame().getPlayer1()
												.getName() + ", "
										+ oldSet.getPlayer1Score() + ":"
										+ oldSet.getPlayer2Score());
							} else if (oldSet.getPlayer2Score() > oldSet
									.getPlayer1Score()) {
								oldSet.setWinner(oldSet.getGame().getPlayer2());

								logger.debug("Determined game set winner: "
										+ oldSet.getGame().getPlayer2()
												.getName() + ", "
										+ oldSet.getPlayer2Score() + ":"
										+ oldSet.getPlayer1Score());
							}
						}
					}

					if (numDTOs > 0) {
						updated = true;
						game.setSets(existingSets);
					}

				} else {
					logger.warn("Player ID mismatch");
				}

				if (updated) {
					int setsPlayed = 0;
					int setsWonByPlayer1 = 0;
					int setsWonByPlayer2 = 0;

					for (IGameSet set : game.getSets()) {
						if (set.hasWinner()
								&& set.getGame().getPlayer1()
										.equals(set.getWinner())) {
							setsPlayed++;
							setsWonByPlayer1++;
						} else if (set.hasWinner()
								&& set.getGame().getPlayer2()
										.equals(set.getWinner())) {
							setsPlayed++;
							setsWonByPlayer2++;
						}
					}

					// we have a game winner if
					// - all sets have been played
					// - one player has won more sets than the other
					// we'll also calculate and write back
					// - (bonus) points for won sets
					// TODO - score ratios for opponents

					if (setsPlayed == game.getSets().size()) {

						if (setsWonByPlayer1 > setsWonByPlayer2) {

							// set player 1 as winner
							game.setWinner(game.getPlayer1());

							logger.debug("Game winner: "
									+ game.getPlayer1().getName() + ", "
									+ setsWonByPlayer1 + ":" + setsWonByPlayer2);

							// add bonus points
							addBonusPoints(
									BonusPointsDistributor.getBonusPoints(
											bonusPointsConfigPath, setsPlayed,
											setsWonByPlayer1),
									game.getPlayer1(), game.getPlayer2());

						} else if (setsWonByPlayer2 > setsWonByPlayer1) {

							// set player 2 as winner
							game.setWinner(game.getPlayer2());

							logger.debug("Game winner: "
									+ game.getPlayer2().getName() + ", "
									+ setsWonByPlayer2 + ":" + setsWonByPlayer1);

							// add bonus points
							addBonusPoints(
									BonusPointsDistributor.getBonusPoints(
											bonusPointsConfigPath, setsPlayed,
											setsWonByPlayer2),
									game.getPlayer2(), game.getPlayer1());
						}
					}

					logger.debug("After: " + game);
					// write back
					gameDAO.updateGame(game);
					success = true;
				}
			}
		}

		return success;
	}

	// switch players if necessary (player1Score becomes player2Score etc.)
	// needed in rare case where client JSON might be mixed up
	private GameDTO alignPlayers(GameDTO unalignedDTO) {
		// log level info because unusual
		logger.info("Unaligned DTO: " + unalignedDTO);

		GameDTO alignedDTO = new GameDTO(unalignedDTO);
		// vice versa
		alignedDTO.setPlayer1Id(unalignedDTO.getPlayer2Id());
		alignedDTO.setPlayer1Name(unalignedDTO.getPlayer2Name());
		alignedDTO.setPlayer2Id(unalignedDTO.getPlayer1Id());
		alignedDTO.setPlayer2Name(unalignedDTO.getPlayer1Name());
		// align set list
		ArrayList<GameSetDTO> alignedSets = new ArrayList<GameSetDTO>();
		for (GameSetDTO set : unalignedDTO.getSets()) {
			logger.info("Unaligned set: " + set);
			// vice versa
			// we have to set any winner to null
			GameSetDTO alignedSet = new GameSetDTO(set.getId(),
					set.getGameId(), 0, set.getPlayer2Score(),
					set.getPlayer1Score(), null);
			logger.info("Aligned set: " + alignedSet);

			alignedSets.add(alignedSet);
		}

		alignedDTO.setSets(alignedSets);
		logger.info("Aligned DTO: " + alignedDTO);

		return alignedDTO;
	}

	private void addBonusPoints(HashMap<String, Integer> bonusPoints,
			IPlayer winner, IPlayer loser) {
		winner.setPoints(winner.getPoints() + bonusPoints.get("winner"));
		loser.setPoints(loser.getPoints() + bonusPoints.get("loser"));

		logger.info("Bonus points: " + winner.getName() + " +"
				+ bonusPoints.get("winner") + " = " + winner.getPoints() + ", "
				+ loser.getName() + " +" + bonusPoints.get("loser") + " = "
				+ loser.getPoints());
	}
}
