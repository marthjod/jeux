package de.fhb.jeux.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRoundSwitchRule;
import de.fhb.jeux.persistence.ShowdownRoundSwitchRule;

@Stateless
@LocalBean
public class RoundSwitchRuleDAO {

    private static Logger logger = Logger.getLogger(RoundSwitchRuleDAO.class);

    @PersistenceContext(unitName = "JeuxEJB")
    private EntityManager em;

    public RoundSwitchRuleDAO() {
    }

    public boolean addRule(IRoundSwitchRule rule) {
        boolean success = false;
        try {
            em.persist(rule);
            success = true;
            // logger.debug("Persisted round-switch-rule " + rule);
        } catch (Exception e) {
            logger.error(rule + ": " + e.getClass().getName() + " "
                    + e.getMessage());
        }
        return success;
    }

    public boolean deleteRule(IRoundSwitchRule rule) {
        boolean success = false;

        if (rule != null) {
            IRoundSwitchRule tempRule = rule;
            try {
                em.remove(rule);
                success = true;
                logger.debug("Deleted rule " + tempRule);
            } catch (Exception e) {
                logger.error("Failed to delete round-switch rule " + tempRule);
                logger.error(e.getClass().getCanonicalName() + " "
                        + e.getMessage());
            }
        }
        return success;
    }

    public List<IRoundSwitchRule> getAllRules() {
        return runQuery("RoundSwitchRule.findAll", null, null);
    }

    public List<IRoundSwitchRule> getRulesForSrcGroup(IGroup group) {
        return runQuery("RoundSwitchRule.findBySrcGroup", "srcGroup", group);
    }

    public List<IRoundSwitchRule> getRulesForDestGroup(IGroup group) {
        return runQuery("RoundSwitchRule.findByDestGroup", "destGroup", group);
    }

    // if paramName = null, paramName and paramGroup are ignored
    private List<IRoundSwitchRule> runQuery(String queryName, String paramName,
            IGroup groupParam) {
        List<IRoundSwitchRule> rules = new ArrayList<>();
        if (queryName != null) {
            TypedQuery<IRoundSwitchRule> query = em.createNamedQuery(queryName,
                    IRoundSwitchRule.class);
            if (paramName != null && groupParam != null) {
                query.setParameter(paramName, groupParam);
            }
            rules = query.getResultList();
        }

        return rules;
    }

    public IRoundSwitchRule getRuleById(int id) {
        IRoundSwitchRule rule = new ShowdownRoundSwitchRule();
        TypedQuery<IRoundSwitchRule> query = em.createNamedQuery(
                "RoundSwitchRule.findById", IRoundSwitchRule.class);
        query.setParameter("id", id);

        try {
            rule = query.getSingleResult();
        } catch (NoResultException e) {
            // reset because callers should test for null
            rule = null;
            logger.error("RSR ID " + id + ": " + e.getMessage() + " ("
                    + e.getClass().getName() + ")");
        }
        return rule;
    }

}
