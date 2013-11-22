package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.dao.GameDAO;

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

}
