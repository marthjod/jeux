package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IRoundSwitchRule;

@Remote
public interface CreateRoundSwitchRuleRemote {
	public void createNewRoundSwitchRule(IRoundSwitchRule rule);
}
