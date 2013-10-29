package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.model.IPlayer;

@Stateless
public class PlayerBean implements PlayerRemote, PlayerLocal {

	@EJB
	private PlayerDAO playerDAO;

	public PlayerBean() {
	}

	@Override
	public List<IPlayer> getAllPlayers() {
		return playerDAO.getAllPlayers();
	}

	@Override
	public IPlayer getPlayerById(int playerId) {
		return playerDAO.getPlayerById(playerId);
	}

}
