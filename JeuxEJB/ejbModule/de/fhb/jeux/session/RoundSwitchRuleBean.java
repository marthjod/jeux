package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.model.IRoundSwitchRule;

@Stateless
@LocalBean
public class RoundSwitchRuleBean implements RoundSwitchRuleRemote, RoundSwitchRuleLocal {

    @EJB
    RoundSwitchRuleDAO roundSwitchRuleDAO;
	
	public RoundSwitchRuleBean() {
    }
    
    public List<IRoundSwitchRule> getAllRoundSwitchRule() {
    	return roundSwitchRuleDAO.getAllRoundSwitchRules();
    }
    
    public IRoundSwitchRule getRoundSwitchRuleById(int roundSwitchRuleId) {
    	return roundSwitchRuleDAO.getRoundSwitchRuleById(roundSwitchRuleId);
    }

}
