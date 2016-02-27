package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.dto.RuleDTO;
import de.fhb.jeux.model.IRoundSwitchRule;

@Stateless
@LocalBean

public class RoundSwitchRuleBean implements RoundSwitchRuleRemote,
		RoundSwitchRuleLocal {

	protected static Logger logger = Logger
			.getLogger(RoundSwitchRuleBean.class);

	@EJB
	RoundSwitchRuleDAO roundSwitchRuleDAO;

	public RoundSwitchRuleBean() {
	}

	@Override
	public List<IRoundSwitchRule> getAllRoundSwitchRules() {
		return roundSwitchRuleDAO.getAllRules();
	}

	@Override
	public List<RuleDTO> getAllRoundSwitchRuleDTOs() {
		List<IRoundSwitchRule> rules = getAllRoundSwitchRules();
		List<RuleDTO> ruleSetsDTO = new ArrayList<RuleDTO>();

		for (IRoundSwitchRule rule : rules) {
			RuleDTO newRoundSwitchRuleDTO = new RuleDTO(
					rule);
			ruleSetsDTO.add(newRoundSwitchRuleDTO);
		}
		return ruleSetsDTO;
	}

	@Override
	public IRoundSwitchRule getRoundSwitchRuleById(int id) {
		return roundSwitchRuleDAO.getRuleById(id);
	}

}
