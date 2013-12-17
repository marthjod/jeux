package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IPlayer;

@Remote
public interface DeletePlayerRemote {

	boolean deletePlayer(IPlayer player);

}
