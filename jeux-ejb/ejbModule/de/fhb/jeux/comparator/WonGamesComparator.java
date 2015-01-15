package de.fhb.jeux.comparator;

import java.util.Comparator;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.model.IPlayer;

public class WonGamesComparator implements Comparator<IPlayer> {

	private static Logger logger = Logger.getLogger(WonGamesComparator.class);

	private PlayerDAO playerDAO;

	// poor man's injection
	public WonGamesComparator(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

	@Override
	public int compare(IPlayer p1, IPlayer p2) {
		IPlayer higherRankedPlayer = null;

		// winner := either won more games OR
		// if same amount of games, better score ratio;
		// still equal? look who won when they played against each other
		if (p1.getWonGames() > p2.getWonGames()) {
			higherRankedPlayer = p1;
		} else if (p1.getWonGames() < p2.getWonGames()) {
			higherRankedPlayer = p2;
		} else {

			if (p1.getScoreRatio() > p2.getScoreRatio()) {
				higherRankedPlayer = p1;
			} else if (p1.getScoreRatio() < p2.getScoreRatio()) {
				higherRankedPlayer = p2;
			} else {
				// direct comparison necessary...
				// must use IDs because we only have flat DTOs here;
				// also have to convert result to DTO
				IPlayer tempPlayer = playerDAO.directComparison(p1.getId(),
						p2.getId());
				if (tempPlayer != null) {
					if (p1.getId() == tempPlayer.getId()) {
						higherRankedPlayer = p1;
					} else if (p2.getId() == tempPlayer.getId()) {
						higherRankedPlayer = p2;
					}

					if (higherRankedPlayer != null) {
						logger.info("Direct comparison won by "
								+ higherRankedPlayer.getName());
					}
				} else {
//					logger.warn("No direct comparison possible for "
//							+ p1.getName() + " and " + p2.getName());
				}
			}
		}

		if (higherRankedPlayer != null) {
			if (p1.equals(higherRankedPlayer)) {
//				logger.info(p1.getName() + " > " + p2.getName());
				return -1;
			} else if (p2.equals(higherRankedPlayer)) {
//				logger.info(p2.getName() + " > " + p1.getName());
				return 1;
			} else {
//				logger.error("Could not determine winner.");
				return 0;
			}
		} else {
//			logger.warn("Cannot determine higher-ranked player.");
			return 0;
		}
	}
}
