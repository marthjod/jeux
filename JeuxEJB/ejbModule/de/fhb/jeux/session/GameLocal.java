package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGroup;

@Local
public interface GameLocal {

	public List<GameDTO> getPlayedGameDTOsInGroup(IGroup group);

	public List<GameDTO> getUnplayedGameDTOsInGroup(IGroup group);

	// public List<IGame> getGamesInGroup(IGroup group);

}
