package de.fhb.jeux.session;

import javax.ejb.Remote;

@Remote
public interface CreateNewGroupRemote {
	public boolean createNewGroup(String jsonRepresentation);
}
