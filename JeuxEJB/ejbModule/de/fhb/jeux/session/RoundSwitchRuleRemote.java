package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.model.IRoundSwitchRule;

@Remote
public interface RoundSwitchRuleRemote {
	
	 public List<IRoundSwitchRule> getAllRoundSwitchRule();
	 public IRoundSwitchRule getRoundSwitchRuleById(int roundSwitchRuleId);

}