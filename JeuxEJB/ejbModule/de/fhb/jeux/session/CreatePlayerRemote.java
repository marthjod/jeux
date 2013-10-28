package de.fhb.jeux.session;

import javax.ejb.Remote;

@Remote
public interface CreatePlayerRemote {
	public boolean createPlayer(String jsonRepresentation);
}
