package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Local
public interface GroupLocal {

    public List<GroupDTO> getAllGroupDTOs();

    public IGroup getGroupById(int groupId);

    public IGroup getGroupByName(String name);

    public List<PlayerDTO> getPlayerDTOsInGroup(IGroup group);

}
