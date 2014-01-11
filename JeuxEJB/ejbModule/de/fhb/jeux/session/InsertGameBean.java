package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.model.IGame;

@Stateless
public class InsertGameBean implements InsertGameBeanRemote,
		InsertGameBeanLocal {

	protected static Logger logger = Logger.getLogger(InsertGameBean.class);

	// "created"
	public static final int INSERT_OK = 201;
	// "conflict"
	public static final int INSERT_CONFLICT = 409;
	// "service unavailable"
	public static final int INSERT_ERR = 503;
	// "internal server error"
	public static final int UNKNOWN_ERR = 500;

	@EJB
	private GameDAO gameDAO;

	public InsertGameBean() {
	}

	@Override
	// TODO return value ~= HTTP status code
	public int insertGame(IGame game) {
		int status = UNKNOWN_ERR;

		// TODO catch different failure cases
		try {
			gameDAO.addGame(game);
			status = INSERT_OK;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			status = INSERT_ERR;
		}

		return status;
	}

}
