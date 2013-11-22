package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.model.IGameSet;

@Local
public interface GameSetLocal {
	
	public List<IGameSet> getAllGameSets();
}