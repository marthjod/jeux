package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.dto.RuleDTO;
import de.fhb.jeux.model.IGroup;

@Remote

public interface CreateRoundSwitchRuleRemote {
	
	public int createRoundSwitchRule(RuleDTO ruleDTO,
			IGroup srcGroup, IGroup destGroup);
}
