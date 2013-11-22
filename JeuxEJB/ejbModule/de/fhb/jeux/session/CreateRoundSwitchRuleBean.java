package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.dto.RoundSwitchRuleDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRoundSwitchRule;
import de.fhb.jeux.persistence.ShowdownRoundSwitchRule;

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
	public boolean createRoundSwitchRule(RoundSwitchRuleDTO ruleDTO,
			IGroup srcGroup, IGroup destGroup) {

		boolean success = false;
		// persist after converting
		IRoundSwitchRule newRule = new ShowdownRoundSwitchRule(ruleDTO);
		// group must be set from here because GroupBean cannot be
		// accessed later on (in the constructor, f.ex.).
		// (CDI's fault)

		if (srcGroup != null && destGroup != null) {
			newRule.setSrcGroup(srcGroup);
			newRule.setDestGroup(destGroup);
			if (ruleDAO.addRoundSwitchRule(newRule)) {
				success = true;
				logger.debug("Added round switch rule " + newRule);
			}
		}

		return success;
	}
}
