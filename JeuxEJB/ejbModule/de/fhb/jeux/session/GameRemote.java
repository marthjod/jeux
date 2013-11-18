package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.model.IGame;

@Remote
public interface GameRemote {
	public List<IGame> getAllGames();
}
