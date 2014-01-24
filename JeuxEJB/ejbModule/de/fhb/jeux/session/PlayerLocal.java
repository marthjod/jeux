package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.model.IPlayer;

@Local
public interface PlayerLocal {

	public IPlayer getPlayerById(int playerId);
}
