package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Remote
public interface GroupRemote {
	public List<GroupDTO> getAllGroupDTOs();

	public IGroup getGroupById(int groupId);

	public List<PlayerDTO> getPlayerDTOsInGroup(IGroup group);
}
