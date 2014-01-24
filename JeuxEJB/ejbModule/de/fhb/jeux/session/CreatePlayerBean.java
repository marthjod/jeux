package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownPlayer;

@Stateless
@SuppressWarnings("ucd")
public class CreatePlayerBean implements CreatePlayerRemote, CreatePlayerLocal {

	protected static Logger logger = Logger.getLogger(CreatePlayerBean.class);

	@EJB
	private PlayerDAO playerDAO;

	public CreatePlayerBean() {
	}

	@Override
	public void createPlayer(PlayerDTO playerDTO, IGroup group) {

		boolean checkOK = false;
		// TODO (MORE) SANITY-CHECKING HERE BEFORE PERSISTING

		// TODO TESTING, REMOVE BEFORE FLIGHT
		checkOK = true;

		if (checkOK) {
			// persist after converting
			IPlayer newPlayer = new ShowdownPlayer(playerDTO, group);
			playerDAO.addPlayer(newPlayer);
			logger.debug("Added player '" + newPlayer.getName() + "'");
		}
	}
}
