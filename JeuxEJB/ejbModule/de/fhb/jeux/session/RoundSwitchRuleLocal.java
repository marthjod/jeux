package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.model.IRoundSwitchRule;

@Local
public interface RoundSwitchRuleLocal {
	
	 public List<IRoundSwitchRule> getAllRoundSwitchRule();
	 public IRoundSwitchRule getRoundSwitchRuleById(int roundSwitchRuleId);

}