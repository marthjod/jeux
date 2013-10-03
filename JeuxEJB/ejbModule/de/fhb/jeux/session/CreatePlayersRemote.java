package de.fhb.jeux.session;

import javax.ejb.Remote;

@Remote
public interface CreatePlayersRemote {
	public boolean createPlayers(String jsonRepresentation);
}
