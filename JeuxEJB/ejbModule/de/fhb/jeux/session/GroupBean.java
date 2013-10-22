package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.model.IGroup;

@Stateless
public class GroupBean implements GroupRemote, GroupLocal {

	@EJB
	private GroupDAO groupDAO;

	public GroupBean() {
	}

	@Override
	public List<IGroup> getAllGroups() {
		return groupDAO.getAllGroups();
	}

}
