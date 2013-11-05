package de.fhb.jeux.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import de.fhb.jeux.mockentity.MockPlayerEntity;
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

	public void deletePlayer(IPlayer player) {
		em.remove(player);
		logger.debug("Deleted player '" + player.getName() + "'");
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
		player = query.getSingleResult();
		return player;
	}
}
