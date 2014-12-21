package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IGame;

@Remote
@SuppressWarnings("ucd")
public interface InsertGameRemote {
	@SuppressWarnings("ucd")
	public int insertGame(IGame game);
}
