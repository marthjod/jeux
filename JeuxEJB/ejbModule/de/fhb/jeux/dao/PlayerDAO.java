package de.fhb.jeux.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownPlayer;

@Stateless
@LocalBean
public class PlayerDAO {

	protected static Logger logger = Logger.getLogger(PlayerDAO.class);

	@PersistenceContext
	private EntityManager em;

	public PlayerDAO() {
	}

	public void addPlayer(IPlayer player) {
		em.persist(player);
		logger.debug("Persisted player '" + player.getName() + "'");
	}

	public boolean deletePlayer(IPlayer player) {
		boolean success = false;
		if (player != null) {
			String playerName = player.getName();
			try {
				em.remove(player);
				success = true;
				logger.debug("Deleted player '" + playerName + "'");
			} catch (Exception e) {
				logger.error("Failed to delete player '" + playerName + "'");
				logger.error(e.getClass().getCanonicalName() + " "
						+ e.getMessage());
			}
		}

		return success;
	}

	public void updatePlayer(IPlayer player) {
		em.merge(player);
	}

	public List<IPlayer> getAllPlayers() {
		List<IPlayer> players = new ArrayList<IPlayer>();

		TypedQuery<IPlayer> query = em.createNamedQuery("Player.findAll",
				IPlayer.class);
		players = query.getResultList();

		return players;
	}

	public IPlayer getPlayerById(int playerId) {
		IPlayer player = new ShowdownPlayer();
		TypedQuery<IPlayer> query = em.createNamedQuery("Player.findById",
				IPlayer.class);
		query.setParameter("id", playerId);
		try {
			player = query.getSingleResult();
		} catch (NoResultException e) {
			// reset because callers should test for null
			player = null;
			logger.error("Player ID " + playerId + ": " + e.getMessage());
		}
		return player;
	}
}
