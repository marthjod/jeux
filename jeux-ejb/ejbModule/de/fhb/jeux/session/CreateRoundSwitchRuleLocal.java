package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.dto.RuleDTO;
import de.fhb.jeux.model.IGroup;

@Local
public interface CreateRoundSwitchRuleLocal {

	public int createRoundSwitchRule(RuleDTO ruleDTO,
			IGroup srcGroup, IGroup destGroup);
}
