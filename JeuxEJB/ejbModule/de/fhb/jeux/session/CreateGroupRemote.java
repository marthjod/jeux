package de.fhb.jeux.session;

import javax.ejb.Remote;

@Remote
public interface CreateGroupRemote {
	public boolean createNewGroup(String jsonRepresentation);
}
