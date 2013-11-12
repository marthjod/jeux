package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.dto.GroupDTO;

@Local
public interface CreateGroupLocal {
	public void createGroup(GroupDTO groupDTO);
}
