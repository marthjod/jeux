package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.dto.RoundSwitchRuleDTO;
import de.fhb.jeux.model.IGroup;

@Remote
public interface CreateRoundSwitchRuleRemote {
	public boolean createRoundSwitchRule(RoundSwitchRuleDTO ruleDTO,
			IGroup srcGroup, IGroup destGroup);
}
