package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IPlayer;

@Remote
@SuppressWarnings("ucd")
public interface PlayerRemote {

	@SuppressWarnings("ucd")
	public IPlayer getPlayerById(int playerId);
}
