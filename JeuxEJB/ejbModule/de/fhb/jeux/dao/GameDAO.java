package de.fhb.jeux.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownGame;

@Stateless
@LocalBean
public class GameDAO {

	protected static Logger logger = Logger.getLogger(GameDAO.class);

	@PersistenceContext
	private EntityManager em;

	public GameDAO() {
	}

	public void addGame(IGame game) {
		em.persist(game);
		logger.debug("Persisted game '" + game.getId() + "'");
	}

	public void deleteGame(IGame game) {
		em.remove(game);
		logger.debug("Deleted game '" + game.getId() + "'");
	}

	public void updateGame(IGame game) {
		em.merge(game);
	}

	// public List<IGame> getGamesInGroup(IGroup group) {
	// List<IGame> games = new ArrayList<IGame>();
	//
	// TypedQuery<IGame> query = em.createNamedQuery("Game.findAllInGroup",
	// IGame.class);
	// query.setParameter("group", group);
	// games = query.getResultList();
	//
	// return games;
	// }

	public List<IGame> getPlayedGamesInGroup(IGroup group) {
		List<IGame> playedGames = new ArrayList<IGame>();

		TypedQuery<IGame> query = em.createNamedQuery("Game.findPlayedInGroup",
				IGame.class);
		query.setParameter("group", group);
		playedGames = query.getResultList();

		return playedGames;
	}

	public List<IGame> getUnplayedGamesInGroup(IGroup group) {
		List<IGame> unplayedGames = new ArrayList<IGame>();

		TypedQuery<IGame> query = em.createNamedQuery(
				"Game.findUnplayedInGroup", IGame.class);
		query.setParameter("group", group);
		unplayedGames = query.getResultList();

		return unplayedGames;
	}

	// for deletion and updates
	public IGame getGameById(int gameId) {
		IGame game = new ShowdownGame();
		TypedQuery<IGame> query = em.createNamedQuery("Game.findById",
				IGame.class);
		query.setParameter("id", gameId);
		game = query.getSingleResult();
		return game;
	}
}
