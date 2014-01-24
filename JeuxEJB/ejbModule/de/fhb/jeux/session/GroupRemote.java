package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Remote
@SuppressWarnings("ucd")
public interface GroupRemote {
	public List<GroupDTO> getAllGroupDTOs();

	@SuppressWarnings("ucd")
	public IGroup getGroupById(int groupId);

	@SuppressWarnings("ucd")
	public List<PlayerDTO> getPlayerDTOsInGroup(IGroup group);
}
