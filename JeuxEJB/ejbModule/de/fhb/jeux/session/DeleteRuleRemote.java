package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IRoundSwitchRule;

@Remote
public interface DeleteRuleRemote {

	public boolean deleteRule(IRoundSwitchRule rule);
}
