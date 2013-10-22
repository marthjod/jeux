package de.fhb.jeux.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.fhb.jeux.model.IGroup;

@Stateless
@LocalBean
public class GroupDAO {

	@PersistenceContext
	private EntityManager em;

	public GroupDAO() {
	}

	public void addGroup(IGroup group) {
		em.persist(group);
	}

	public void deleteGroup(IGroup group) {
		em.remove(group);
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
}
