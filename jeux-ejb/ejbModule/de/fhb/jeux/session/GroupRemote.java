package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Remote

public interface GroupRemote {

    public List<GroupDTO> getAllGroupDTOs(boolean sort);

    public IGroup getGroupById(int groupId);

    public IGroup getGroupByName(String name);

    public List<PlayerDTO> getPlayerDTOsInGroup(IGroup group);

}
