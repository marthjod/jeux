package de.fhb.jeux.config;

import java.util.HashMap;

import org.jboss.logging.Logger;

// reads/converts bonus points rules and provides convenient access
public class BonusPointsDistributor {

    private static Logger logger = Logger
            .getLogger(BonusPointsDistributor.class);

    private BonusPointsDistribution config;

    public BonusPointsDistributor(BonusPointsDistribution config) {
        this.config = config;
    }

    public HashMap<String, Integer> getBonusPoints(int totalSetsPlayed,
            int setsWonByWinner) {

        HashMap<String, Integer> bonusPoints = new HashMap<>();
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
