package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.model.IRoundSwitchRule;

@Stateless
public class CreateRoundSwitchRuleBean implements CreateRoundSwitchRuleRemote,
		CreateRoundSwitchRuleLocal {

	protected static Logger logger = Logger
			.getLogger(CreateRoundSwitchRuleBean.class);

	@EJB
	private RoundSwitchRuleDAO ruleDAO;

	public CreateRoundSwitchRuleBean() {
	}

	@Override
	public void createNewRoundSwitchRule(IRoundSwitchRule rule) {
		ruleDAO.addRoundSwitchRule(rule);
		logger.debug("Added round switch rule " + rule.toString());
	}
}
