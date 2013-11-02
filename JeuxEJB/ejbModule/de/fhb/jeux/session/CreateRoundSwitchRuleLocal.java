package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.model.IRoundSwitchRule;

@Local
public interface CreateRoundSwitchRuleLocal {

	public void createNewRoundSwitchRule(IRoundSwitchRule rule);
}
