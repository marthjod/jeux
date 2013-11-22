package de.fhb.jeux.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IGameSet;
import de.fhb.jeux.persistence.ShowdownGameSet;

@Stateless
@LocalBean
public class GameSetDAO {

	protected static Logger logger = Logger.getLogger(GameSetDAO.class);
	
	@PersistenceContext
	private EntityManager em;

		public void addGameSet(IGameSet gameSet) {
			em.persist(gameSet);
			logger.debug("Persisted gameSet '" + gameSet.getId() + "'");
		}

		public void deleteGameSet(IGameSet gameSet) {
			em.remove(gameSet);
			logger.debug("Deleted gameSet '" + gameSet.getId() + "'");
		}

		public void updateGameSet(IGameSet gameSet) {
			em.merge(gameSet);
		}

		public List<IGameSet> getAllGameSets() {
			List<IGameSet> gameSets = new ArrayList<IGameSet>();

			TypedQuery<IGameSet> query = em.createNamedQuery("GameSet.findAll",
					IGameSet.class);
			gameSets = query.getResultList();

			return gameSets;
		}

		public IGameSet getGameById(int gameSetId) {
			IGameSet gameSet = new ShowdownGameSet();
			TypedQuery<IGameSet> query = em.createNamedQuery("GameSet.findById",
					IGameSet.class);
			query.setParameter("id", gameSetId);
			gameSet = query.getSingleResult();
			return gameSet;
		}
	}



