package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.model.IPlayer;

@Remote
public interface PlayerRemote {
	public List<IPlayer> getAllPlayers();

	public IPlayer getPlayerById(int playerId);
}
