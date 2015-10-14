package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.model.IGame;

@Local

public interface InsertGameLocal {

	public int insertGame(IGame game);

}
