package de.fhb.jeux.config;

import java.util.List;
import org.jboss.logging.Logger;

// holds multiple single bonus points distribution rules in a list
public class BonusPointsDistribution {

    private static Logger logger = Logger
            .getLogger(BonusPointsDistribution.class);

    private List<BonusPointsRule> ruleList;

    public BonusPointsDistribution(List<BonusPointsRule> rules) {
        ruleList = rules;
        logger.info("Created bonus points distribution rules object");
    }

    public List<BonusPointsRule> getRules() {
        return ruleList;
    }
}
