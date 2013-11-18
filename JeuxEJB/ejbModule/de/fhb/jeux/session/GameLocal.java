package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.model.IGame;

@Local
public interface GameLocal {
	
	public List<IGame> getAllGames();

}
