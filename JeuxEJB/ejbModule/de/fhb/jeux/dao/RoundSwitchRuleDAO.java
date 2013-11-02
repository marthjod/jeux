package de.fhb.jeux.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IRoundSwitchRule;

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
}
