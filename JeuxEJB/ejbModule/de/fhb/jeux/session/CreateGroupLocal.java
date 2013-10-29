package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.model.IGroup;

@Local
public interface CreateGroupLocal {
	public void createNewGroup(IGroup group);
}
