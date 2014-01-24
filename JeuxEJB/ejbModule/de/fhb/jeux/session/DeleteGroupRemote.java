package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IGroup;

@Remote
@SuppressWarnings("ucd")
public interface DeleteGroupRemote {
	@SuppressWarnings("ucd")
	public int deleteGroup(IGroup group);
}
