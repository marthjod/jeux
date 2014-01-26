package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GroupDTO;

@Remote
@SuppressWarnings("ucd")
public interface CreateGroupRemote {

	@SuppressWarnings("ucd")
	public boolean createGroup(GroupDTO groupDTO);
}
