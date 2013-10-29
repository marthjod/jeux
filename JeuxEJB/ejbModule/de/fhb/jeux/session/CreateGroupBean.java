package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.model.IGroup;

@Stateless
public class CreateGroupBean implements CreateGroupRemote, CreateGroupLocal {

	protected static Logger logger = Logger.getLogger(CreateGroupBean.class);

	@EJB
	private GroupDAO groupDAO;

	public CreateGroupBean() {
	}

	@Override
	public void createNewGroup(IGroup group) {
		// persist new Group
		groupDAO.addGroup(group);
		logger.debug("Added group '" + group.getName() + "'");
	}
}
