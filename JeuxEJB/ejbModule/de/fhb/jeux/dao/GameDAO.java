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
import de.fhb.jeux.persistence.ShowdownGame;

@Stateless
@LocalBean
public class GameDAO {
	
	protected static Logger logger = Logger.getLogger(GameDAO.class);
	
	@PersistenceContext
	private EntityManager em;
	
	public GameDAO(){
		
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

	public List<IGame> getAllGames() {
		List<IGame> games = new ArrayList<IGame>();

		TypedQuery<IGame> query = em.createNamedQuery("Game.findAll",
				IGame.class);
		games = query.getResultList();

		return games;
	}

	public IGame getGameById(int gameId) {
		IGame group = new ShowdownGame();
		TypedQuery<IGame> query = em.createNamedQuery("Game.findById",
				IGame.class);
		query.setParameter("id", gameId);
		group = query.getSingleResult();
		return group;
	}
}


