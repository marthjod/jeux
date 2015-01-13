package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.dto.RoundSwitchRuleDTO;
import de.fhb.jeux.model.IGroup;

@Local
public interface CreateRoundSwitchRuleLocal {

	public int createRoundSwitchRule(RoundSwitchRuleDTO ruleDTO,
			IGroup srcGroup, IGroup destGroup);
}
