package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GameSetDAO;
import de.fhb.jeux.dto.GameSetDTO;
import de.fhb.jeux.model.IGameSet;

@Stateless
public class GameSetBean implements GameSetRemote, GameSetLocal {

	@EJB
	private GameSetDAO gameSetDAO;

	public GameSetBean() {
	}

	@Override
	public List<IGameSet> getAllGameSets() {
		return gameSetDAO.getAllGameSets();
	}

	// flat DTOs for response output
	@Override
	public List<GameSetDTO> getAllGameSetDTOs() {
		List<IGameSet> gameSets = getAllGameSets();
		List<GameSetDTO> gameSetsDTO = new ArrayList<GameSetDTO>();

		for (IGameSet gameSet : gameSets) {
			GameSetDTO newGameSetDTO = new GameSetDTO(gameSet);
			gameSetsDTO.add(newGameSetDTO);
		}
		return gameSetsDTO;
	}

}
