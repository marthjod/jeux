package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.model.IGroup;

@Stateless
public class DeleteGroupBean implements DeleteGroupRemote, DeleteGroupLocal {

	@EJB
	private GroupDAO groupDAO;

	public DeleteGroupBean() {
	}

	@Override
	public boolean deleteGroup(IGroup group) {
		boolean deleted = false;
		groupDAO.deleteGroup(group);

		// em.remove() returns void...
		deleted = true;

		return deleted;
	}

}
