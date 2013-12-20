package de.fhb.jeux.config;

import java.util.List;

// holds multiple single bonus points distribution rules in a list
public class BonusPointsDistribution {
	private List<BonusPointsRule> ruleList;

	public BonusPointsDistribution(List<BonusPointsRule> rules) {
		ruleList = rules;
	}

	public List<BonusPointsRule> getRules() {
		return ruleList;
	}
}
