package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IGroup;

@Remote

public interface DeleteGroupRemote {
	
	public int deleteGroup(IGroup group);
}
