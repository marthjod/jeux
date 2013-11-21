package de.fhb.jeux.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IRoundSwitchRule;
import de.fhb.jeux.persistence.ShowdownRoundSwitchRule;

@Stateless
@LocalBean
public class RoundSwitchRuleDAO {

	protected static Logger logger = Logger.getLogger(RoundSwitchRuleDAO.class);

	@PersistenceContext
	private EntityManager em;

	public RoundSwitchRuleDAO() {
	}

	public void addRoundSwitchRule(IRoundSwitchRule rule) {
		em.persist(rule);
		logger.debug("Persisted RoundSwitchRule '" + rule.toString() + "'");
	}

	public void deleteRoundSwitchRule(IRoundSwitchRule rule) {
		em.remove(rule);
		logger.debug("Deleted RoundSwitchRule '" + rule.toString() + "'");
	}

	public void updateRoundSwitchRule(IRoundSwitchRule rule) {
		em.merge(rule);
	}
	
	public List<IRoundSwitchRule> getAllRoundSwitchRules() {
		List<IRoundSwitchRule> rules = new ArrayList<IRoundSwitchRule>();

		TypedQuery<IRoundSwitchRule> query = em.createNamedQuery("RoundSwitchRule.findAll",
				IRoundSwitchRule.class);
		rules = query.getResultList();

		return rules;
	}

	public IRoundSwitchRule getRoundSwitchRuleById(int roundSwitchRuleId) {
		IRoundSwitchRule rule = new ShowdownRoundSwitchRule();
		TypedQuery<IRoundSwitchRule> query = em.createNamedQuery("RoundSwitchRule.findById",
				IRoundSwitchRule.class);
		query.setParameter("id", roundSwitchRuleId);
		rule = query.getSingleResult();
		return rule;
	}
}
