package de.fhb.jeux.session;

import javax.ejb.Local;

@Local
public interface CreateGroupLocal {
	public boolean createNewGroup(String jsonRepresentation);
}
