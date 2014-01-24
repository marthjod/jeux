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

@Stateless
@LocalBean
public class RoundSwitchRuleDAO {

	private static Logger logger = Logger.getLogger(RoundSwitchRuleDAO.class);

	@PersistenceContext
	private EntityManager em;

	public RoundSwitchRuleDAO() {
	}

	public boolean addRoundSwitchRule(IRoundSwitchRule rule) {
		boolean success = false;
		try {
			em.persist(rule);
			success = true;
			logger.debug("Persisted RoundSwitchRule " + rule);
		} catch (Exception e) {
			logger.error(rule + ": " + e.getMessage());
		}
		return success;
	}

	public List<IRoundSwitchRule> getAllRoundSwitchRules() {
		List<IRoundSwitchRule> rules = new ArrayList<IRoundSwitchRule>();

		TypedQuery<IRoundSwitchRule> query = em.createNamedQuery(
				"RoundSwitchRule.findAll", IRoundSwitchRule.class);
		rules = query.getResultList();

		return rules;
	}
}
