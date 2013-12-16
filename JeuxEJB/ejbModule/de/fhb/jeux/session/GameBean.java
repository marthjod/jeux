package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGame;

@Stateless
public class GameBean implements GameRemote, GameLocal {

	@EJB
	private GameDAO gameDAO;

	public GameBean() {
	}

	@Override
	public List<IGame> getAllGames() {
		return gameDAO.getAllGames();
	}

	// flat DTOs for response output
	public List<GameDTO> getAllGameDTOs() {
		List<GameDTO> gamesDTO = new ArrayList<GameDTO>();

		for (IGame game : getAllGames()) {
			gamesDTO.add(new GameDTO(game));
		}

		return gamesDTO;
	}

}
