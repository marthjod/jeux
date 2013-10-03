package de.fhb.jeux.session;

import javax.ejb.Local;

@Local
public interface CreatePlayersLocal {
	public boolean createPlayers(String jsonRepresentation);
}
