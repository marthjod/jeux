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

	// mimick HTTP statuses
	// 409 = violated constraints
	public static final int DELETION_CONFLICT = 409;
	public static final int DELETION_OK = 200;
	public static final int DELETION_UNKNOWN_ERR = 500;

	public GroupDAO() {
	}

	public void addGroup(IGroup group) {
		em.persist(group);
		logger.debug("Persisted group '" + group.getName() + "'");
	}

	public int deleteGroup(IGroup group) {
		int result = DELETION_UNKNOWN_ERR;
		if (group != null) {
			// save for debug message b/c group object may be removed then
			// already
			String groupName = group.getName();

			try {
				em.remove(group);
				result = DELETION_OK;
				logger.debug("Deleted group '" + groupName + "'");
			} catch (RuntimeException e) {
				// TODO
				// PersistenceException, MySQLConstraintViolatedEx thrown only
				// after em.remove() ?!!
				result = DELETION_CONFLICT;
				logger.error("Deleting group '" + groupName + "': "
						+ e.getClass().getCanonicalName() + " "
						+ e.getMessage());
			} catch (Exception e) {
				logger.error("Deleting group '" + groupName + "': "
						+ e.getClass().getCanonicalName() + " "
						+ e.getMessage());
			}
		}
		return result;
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
