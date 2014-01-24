package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.model.IGame;

@Stateless
public class InsertGameBean implements InsertGameRemote, InsertGameLocal {

	private static Logger logger = Logger.getLogger(InsertGameBean.class);

	// 201 Created
	public static final int INSERT_OK = 201;
	// 409 Cconflict
	public static final int INSERT_CONFLICT = 409;
	// 503 Service Unavailable
	public static final int INSERT_ERR = 503;
	// 500 Internal Server Error
	// package visibility only
	static final int UNKNOWN_ERR = 500;

	@EJB
	private GameDAO gameDAO;

	public InsertGameBean() {
	}

	private boolean gameExists(IGame game) {
		boolean exists = false;

		for (IGame g : gameDAO.getGamesInGroup(game.getGroup())) {
			if (g.equals(game)) {
				exists = true;
				break;
			}
		}

		return exists;
	}

	@Override
	public int insertGame(IGame game) {
		int status = UNKNOWN_ERR;

		if (!gameExists(game)) {
			// TODO catch more/addt'l failure cases
			try {
				gameDAO.addGame(game);
				status = INSERT_OK;
			} catch (Exception e) {
				logger.error(e.getClass().getName() + ": " + e.getMessage());
				status = INSERT_ERR;
			}
		} else {
			status = INSERT_CONFLICT;
		}

		return status;
	}
}
