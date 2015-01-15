package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.model.IGroup;

@Stateless
@SuppressWarnings("ucd")
public class DeleteGroupBean implements DeleteGroupRemote, DeleteGroupLocal {

	@EJB
	private GroupDAO groupDAO;

	public DeleteGroupBean() {
	}

	@Override
	// return value ~= HTTP status code
	public int deleteGroup(IGroup group) {
		return groupDAO.deleteGroup(group);
	}
}
