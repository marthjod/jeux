package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;

@Stateless
public class GameBean implements GameRemote, GameLocal {

	@EJB
	private GameDAO gameDAO;

	public GameBean() {
	}

	private List<GameDTO> getGameDTOsInGroup(IGroup group) {
		List<GameDTO> gameDTOs = new ArrayList<GameDTO>();

		for (IGame game : getGamesInGroup(group)) {
			gameDTOs.add(new GameDTO(game));
		}

		return gameDTOs;
	}

	@Override
	public List<IGame> getGamesInGroup(IGroup group) {
		return gameDAO.getGamesInGroup(group);
	}

	public List<GameDTO> getPlayedGameDTOsInGroup(IGroup group) {
		List<GameDTO> allGames = getGameDTOsInGroup(group);
		List<GameDTO> playedGames = new ArrayList<GameDTO>();

		for (GameDTO game : allGames) {
			if (game.getWinnerId() != 0) {
				playedGames.add(game);
			}
		}

		return playedGames;
	}

}
