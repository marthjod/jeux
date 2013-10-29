package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.model.IPlayer;

@Local
public interface PlayerLocal {
	public List<IPlayer> getAllPlayers();

	public IPlayer getPlayerById(int playerId);
}
