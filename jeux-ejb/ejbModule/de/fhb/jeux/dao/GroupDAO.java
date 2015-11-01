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

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownPlayer;

@Stateless
@LocalBean
public class GroupDAO {

    private static Logger logger = Logger.getLogger(GroupDAO.class);

    @PersistenceContext(unitName = "JeuxEJB")
    private EntityManager em;

    // mimick HTTP statuses
    // 409 = violated constraints
    public static final int DELETION_CONFLICT = 409;
    public static final int DELETION_OK = 200;
    private static final int DELETION_UNKNOWN_ERR = 500;

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
                // PersistenceException, MySQLConstraintViolatedEx thrown only
                // *after* em.remove() ?!!
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

    // returns null if non-existant or failure
    public IGroup getGroupById(int groupId) {
        IGroup group = null;
        TypedQuery<IGroup> query = em.createNamedQuery("Group.findById",
                IGroup.class);
        query.setParameter("id", groupId);

        try {
            group = query.getSingleResult();
        } catch (NoResultException e) {
            // reset because callers should test for null
            group = null;
            logger.error("Group ID " + groupId + ": " + e.getClass().getName()
                    + " " + e.getMessage());
        }
        return group;
    }

    public IGroup getGroupByName(String name) {
        IGroup group = null;
        TypedQuery<IGroup> query = em.createNamedQuery("Group.findByName",
                IGroup.class);
        query.setParameter("name", name);

        try {
            group = query.getSingleResult();
        } catch (NoResultException e) {
            // reset because callers should test for null
            group = null;
            logger.error("Group '" + name + "': " + e.getClass().getName()
                    + " " + e.getMessage());
        }
        return group;
    }

    // private because handles PEs,
    // but only DTOs should leave this layer
    private List<IGroup> getAllGroups() {
        List<IGroup> groups = new ArrayList<IGroup>();

        TypedQuery<IGroup> query = em.createNamedQuery("Group.findAll",
                IGroup.class);
        groups = query.getResultList();

        return groups;
    }

    public List<GroupDTO> getAllGroupDTOs() {
        List<GroupDTO> groupDTOs = new ArrayList<GroupDTO>();
        List<IGroup> groups = getAllGroups();

        for (IGroup group : groups) {
            // populate list of group DTOs from Persistent Entities
            groupDTOs.add(new GroupDTO(group));
        }

        return groupDTOs;
    }

    public List<ShowdownPlayer> getPlayersInGroup(IGroup group) {
        List<ShowdownPlayer> players = new ArrayList<ShowdownPlayer>();
        if (group != null) {
            players = group.getPlayers();
        }
        return players;
    }

    public List<IGroup> getGroupsInRound(int roundId, boolean completed) {
        List<IGroup> groups = new ArrayList<IGroup>();
        TypedQuery<IGroup> query = null;

        StringBuilder sb = new StringBuilder();

        if (completed) {
            query = em.createNamedQuery("Group.findCompleteInRound",
                    IGroup.class);
            sb.append("Completed");
        } else {
            query = em.createNamedQuery("Group.findIncompleteInRound",
                    IGroup.class);
            sb.append("Incomplete");
        }

        if (query != null) {
            query.setParameter("roundId", roundId);
            groups = query.getResultList();
        }

        sb.append(" groups in round ");
        sb.append(roundId);
        sb.append(": ");
        sb.append(groups.size());
        sb.append(".");
        logger.debug(sb.toString());

        return groups;
    }

    // check if round which this group is in is over
    public boolean roundFinished(int roundId) {
        return getGroupsInRound(roundId, false).isEmpty();
    }
}
