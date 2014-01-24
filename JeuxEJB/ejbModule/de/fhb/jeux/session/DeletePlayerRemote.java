package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IPlayer;

@Remote
@SuppressWarnings("ucd")
public interface DeletePlayerRemote {

	@SuppressWarnings("ucd")
	boolean deletePlayer(IPlayer player);

}
