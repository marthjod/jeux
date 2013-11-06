package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GroupDTO;

@Remote
public interface CreateGroupRemote {
	public void createNewGroup(GroupDTO groupDTO);
}
