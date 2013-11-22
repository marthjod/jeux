package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGame;

@Remote
public interface GameRemote {
	public List<IGame> getAllGames();

	public List<GameDTO> getAllGameDTOs();
}
