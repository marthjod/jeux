package de.fhb.jeux.session;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
		// TODO this is the current best-effort algorithm
		// with known weaknesses
		if (shuffledMode) {
			Deque<IPlayer> breakroom = new ArrayDeque<IPlayer>();

			while ((long) games.size() < maxGames) {
				for (IPlayer player1 : group.getPlayers()) {
					for (IPlayer player2 : group.getPlayers()) {
						if (player1.equals(player2)) {
							// cannot play against oneself
							continue;
						}

						newGame = new ShowdownGame(group, player1, player2);

						if (!contains(games, newGame)) {
							if (!breakroom.contains(player1)
									&& !breakroom.contains(player2)) {

								// in this case, add them immediately to a game,
								// then to the breakroom
								games.add(newGame);
								breakroom.add(player1);
								breakroom.add(player2);

								// logger.debug("Added ["
								// + newGame.getPlayer1().getName() + ", "
								// + newGame.getPlayer2().getName() + "]");

							} else if (breakroom.contains(player1)
									&& !breakroom.contains(player2)) {
								// don't use current player2 for
								// this loop anymore
								break;
							} else {
								// if (breakroom.peekFirst() != null) {
								// logger.debug("Break over for "
								// + breakroom.peekFirst().getName());
								// }
								// "FIFO" a breakroom player:
								// the first one who came in becomes
								// available again now
								breakroom.pollFirst();
							}
						}
					}
				}
			}

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
		// for (IGame game : games) {
		// // logger.debug(game);
		// }

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

	@Override
	public String getShuffledGamesList(IGroup group) {
		long maxGames = 0L;
		StringBuilder sb = new StringBuilder();
		
		if (group.getPlayers().size() >= 2) {
			maxGames = maxGames(group.getPlayers().size());
		}

		if (group != null && maxGames > 0) {
			List<IGame> games = calcGamesForGroup(group, true);

			sb.append("-- Calculated ");
			sb.append(games.size());
			sb.append(" of projected ");
			sb.append(maxGames);
			sb.append(" game(s) in shuffled mode.");
			sb.append("\r\n\r\n");

			for (IGame game : games) {
				sb.append(game.getPlayer1().getName());
				sb.append(" vs. ");
				sb.append(game.getPlayer2().getName());
				sb.append("\r\n");
			}
		} else {
			sb.append("Cannot calculate games, group does not exist or has less than 2 players.");
		}

		return sb.toString();
	}

}
