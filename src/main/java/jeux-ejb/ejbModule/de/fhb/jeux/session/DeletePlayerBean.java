package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.model.IPlayer;

@Stateless
@SuppressWarnings("ucd")
public class DeletePlayerBean implements DeletePlayerRemote, DeletePlayerLocal {

	@EJB
	private PlayerDAO playerDAO;

	public DeletePlayerBean() {
	}

	@Override
	public boolean deletePlayer(IPlayer player) {
		return playerDAO.deletePlayer(player);
	}
}
