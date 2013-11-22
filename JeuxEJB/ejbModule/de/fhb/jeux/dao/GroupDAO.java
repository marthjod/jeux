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
import de.fhb.jeux.persistence.ShowdownGroup;

@Stateless
@LocalBean
public class GroupDAO {

	protected static Logger logger = Logger.getLogger(GroupDAO.class);

	@PersistenceContext
	private EntityManager em;

	public GroupDAO() {
	}

	public void addGroup(IGroup group) {
		em.persist(group);
		logger.debug("Persisted group '" + group.getName() + "'");
	}

	public boolean deleteGroup(IGroup group) {
		boolean success = false;
		if (group != null) {
			try {
				em.remove(group);
				success = true;
				logger.debug("Deleted group '" + group.getName() + "'");
			} catch (Exception e) {
				logger.error("Deleting group '" + group.getName() + "': "
						+ e.getMessage());
			}
		}
		return success;
	}

	public void updateGroup(IGroup group) {
		em.merge(group);
	}

	public List<IGroup> getAllGroups() {
		List<IGroup> groups = new ArrayList<IGroup>();

		TypedQuery<IGroup> query = em.createNamedQuery("Group.findAll",
				IGroup.class);
		groups = query.getResultList();

		return groups;
	}

	public IGroup getGroupById(int groupId) {
		IGroup group = new ShowdownGroup();
		TypedQuery<IGroup> query = em.createNamedQuery("Group.findById",
				IGroup.class);
		query.setParameter("id", groupId);
		try {
			group = query.getSingleResult();
		} catch (NoResultException e) {
			// reset because callers should test for null
			group = null;
			logger.error("Group ID " + groupId + ": " + e.getMessage());
		}
		return group;
	}
}
