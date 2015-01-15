package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.model.IRoundSwitchRule;

@Stateless
public class DeleteRuleBean implements DeleteRuleRemote, DeleteRuleLocal {

	@EJB
	private RoundSwitchRuleDAO ruleDAO;

	public DeleteRuleBean() {
	}

	@Override
	public boolean deleteRule(IRoundSwitchRule rule) {
		return ruleDAO.deleteRule(rule);
	}
}
