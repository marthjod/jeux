package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IGame;

@Remote
public interface InsertGameBeanRemote {
	public int insertGame(IGame game);
}
