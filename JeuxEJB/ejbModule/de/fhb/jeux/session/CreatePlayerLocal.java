package de.fhb.jeux.session;

import javax.ejb.Local;

@Local
public interface CreatePlayerLocal {
	public boolean createPlayer(String jsonRepresentation, int groupId);
}
