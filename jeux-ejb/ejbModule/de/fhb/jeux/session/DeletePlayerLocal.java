package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.model.IPlayer;

@Local
public interface DeletePlayerLocal {
	boolean deletePlayer(IPlayer player);
}
