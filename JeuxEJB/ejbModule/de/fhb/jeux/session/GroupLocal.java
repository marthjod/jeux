package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

@Local
public interface GroupLocal {
	public List<IGroup> getAllGroups();

	public IGroup getGroupById(int groupId);

	public List<IPlayer> getPlayersInGroup(IGroup group);

	public int getGroupSize(IGroup group);
}
