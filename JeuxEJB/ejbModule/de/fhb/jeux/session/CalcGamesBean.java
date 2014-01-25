package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.jboss.logging.Logger;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownGame;

@Stateless
public class CalcGamesBean implements CalcGamesRemote, CalcGamesLocal {

	private static Logger logger = Logger.getLogger(CalcGamesBean.class);

	@EJB
	private InsertGameLocal insertGameBean;

	// HTTP 428 Precondition Required
	public static final int TOO_FEW_GROUP_MEMBERS = 428;
	// HTTP 501 Not Implemented
	public static final int CALC_ERR = 501;

	public CalcGamesBean() {
	}

	private long maxGames(int numPlayers) {
		return ArithmeticUtils.binomialCoefficient(numPlayers, 2);
	}

	private boolean contains(List<IGame> games, IGame newGame) {
		boolean contained = false;

		for (IGame existingGame : games) {
			if (existingGame.equals(newGame)) {
				contained = true;
				break;
			}
		}

		return contained;
	}

	private List<IGame> calcGamesForGroup(IGroup group, boolean shuffledMode) {
		List<IGame> games = new ArrayList<IGame>();
		IGame newGame = null;

		long maxGames = maxGames(group.getPlayers().size());

		// use "shuffled mode" for fair order of games
		// to be played one after the other
		if (shuffledMode) {
			// TODO implement shuffled mode #business-logic
		} else {
			// only calculate games, regardless of specific order
			// faster
			while ((long) games.size() < maxGames) {
				for (IPlayer player1 : group.getPlayers()) {
					for (IPlayer player2 : group.getPlayers()) {
						if (player1.equals(player2)) {
							// cannot play against oneself
							continue;
						}

						newGame = new ShowdownGame(group, player1, player2);
						if (!contains(games, newGame)) {
							games.add(newGame);
						}
					}
				}
			}
		}

		String msg = "Calculated " + games.size() + " of projected " + maxGames
				+ " game(s)";
		if (games.size() != maxGames) {
			logger.warn(msg);
		} else {
			logger.debug(msg);
		}

		// DEBUG
		for (IGame game : games) {
			logger.debug(game);
		}

		return games;
	}

	@Override
	public int writeGamesForGroup(IGroup group, boolean shuffledMode) {
		int status = InsertGameBean.UNKNOWN_ERR;

		// we need at least 2 players in a group
		if (group.getPlayers().size() >= 2) {
			List<IGame> games = calcGamesForGroup(group, shuffledMode);
			if (games.size() > 0) {
				for (IGame game : games) {
					status = insertGameBean.insertGame(game);
					if (status != InsertGameBean.INSERT_OK) {
						// bailing
						break;
					}
				}

			} else {
				status = CALC_ERR;
			}
		} else {
			status = TOO_FEW_GROUP_MEMBERS;
		}

		return status;
	}

}
