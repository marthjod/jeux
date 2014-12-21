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

@Stateless
@LocalBean
@SuppressWarnings("ucd")
// Bean visibility must be public
public class GameSetDAO {

	private static Logger logger = Logger.getLogger(GameSetDAO.class);

	@PersistenceContext
	private EntityManager em;

	// EJB business method must be public
	public void deleteGameSet(IGameSet gameSet) {
		em.remove(gameSet);
		logger.debug("Deleted gameSet '" + gameSet.getId() + "'");
	}

	public List<IGameSet> getAllGameSets() {
		List<IGameSet> gameSets = new ArrayList<IGameSet>();

		TypedQuery<IGameSet> query = em.createNamedQuery("GameSet.findAll",
				IGameSet.class);
		gameSets = query.getResultList();

		return gameSets;
	}

}
