package de.fhb.jeux.session;

import javax.ejb.Local;

@Local
public interface CreateNewGroupLocal {
	public boolean createNewGroup(String jsonRepresentation);
}
