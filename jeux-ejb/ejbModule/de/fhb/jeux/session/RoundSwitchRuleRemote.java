package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.RuleDTO;
import de.fhb.jeux.model.IRoundSwitchRule;

@Remote

public interface RoundSwitchRuleRemote {

	public List<IRoundSwitchRule> getAllRoundSwitchRules();

	public List<RuleDTO> getAllRoundSwitchRuleDTOs();

	public IRoundSwitchRule getRoundSwitchRuleById(int id);

}
