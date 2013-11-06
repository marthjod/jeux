package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownGroup;

@Stateless
public class CreateGroupBean implements CreateGroupRemote, CreateGroupLocal {

	protected static Logger logger = Logger.getLogger(CreateGroupBean.class);

	@EJB
	private GroupDAO groupDAO;

	public CreateGroupBean() {
	}

	@Override
	public void createNewGroup(GroupDTO groupDTO) {

		// TODO (MORE) SANITY-CHECKING HERE BEFORE PERSISTING

		boolean checkOK = false;

		// creating "completed" groups is disallowed
		if (groupDTO.getMinSets() <= groupDTO.getMaxSets()
				&& !groupDTO.isCompleted()) {
			checkOK = true;
		}

		if (checkOK) {
			// persist new Group entity after having
			// converted it from DTO
			IGroup newGroup = new ShowdownGroup(groupDTO);
			groupDAO.addGroup(newGroup);
			logger.debug("Added group '" + newGroup.getName() + "'");
		}
	}
}
