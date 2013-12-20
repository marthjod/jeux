package de.fhb.jeux.config;

import java.util.HashMap;

import org.jboss.logging.Logger;

// reads/converts bonus points rules and provides convenient access
public class BonusPointsDistributor {

	protected static Logger logger = Logger
			.getLogger(BonusPointsDistributor.class);

	public static HashMap<String, Integer> getBonusPoints(
			BonusPointsDistribution config, int totalSetsPlayed,
			int setsWonByWinner) {

		HashMap<String, Integer> bonusPoints = new HashMap<String, Integer>();
		// defaults
		bonusPoints.put("winner", 0);
		bonusPoints.put("loser", 0);

		if (config != null) {
			for (BonusPointsRule rule : config.getRules()) {
				if (totalSetsPlayed == rule.getTotalSets()
						&& setsWonByWinner == rule.getSetsWonByWinner()) {
					// award bonus points accordingly
					bonusPoints.put("winner", rule.getBonusPointsWinner());
					bonusPoints.put("loser", rule.getBonusPointsLoser());
				}
			}
		} else {
			logger.error("No bonus points rules available.");
		}

		if (bonusPoints.get("winner") == 0 && bonusPoints.get("loser") == 0) {
			logger.warn("No bonus points awarded.");
		}

		return bonusPoints;
	}
}
