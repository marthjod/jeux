package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.model.IGroup;

@Local
public interface GroupLocal {
	public List<IGroup> getAllGroups();

	public IGroup getGroupById(int groupId);
}
