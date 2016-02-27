package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.dto.RuleDTO;
import de.fhb.jeux.model.IRoundSwitchRule;

@Local
public interface RoundSwitchRuleLocal {

	public List<IRoundSwitchRule> getAllRoundSwitchRules();

	public List<RuleDTO> getAllRoundSwitchRuleDTOs();

	public IRoundSwitchRule getRoundSwitchRuleById(int id);

}
