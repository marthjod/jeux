package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;

@Stateless
public class CalcGamesBean implements CalcGamesRemote, CalcGamesLocal {

	protected static Logger logger = Logger.getLogger(CalcGamesBean.class);

	// "not implemented"
	public static final int CALC_ERR = 501;

	public CalcGamesBean() {
	}

	private List<IGame> calcGamesForGroup(IGroup group, boolean shuffledMode) {
		List<IGame> games = new ArrayList<IGame>();

		// use "shuffled mode" for fair order of games to be played one after
		// the
		// other
		if (shuffledMode) {
			// TODO
		} else {
			// only calculate games, no specific order

		}

		return games;
	}

	@Override
	public int writeGamesForGroup(IGroup group, boolean shuffledMode) {
		int status = InsertGameBean.UNKNOWN_ERR;

		List<IGame> games = calcGamesForGroup(group, shuffledMode);
		if (games.size() > 0) {
			for (IGame game : games) {
				status = insertGameBean.insertGame(game);
				if (status != InsertGameBean.INSERT_OK) {
					// bailing
					break;
				}
			}

		} else {
			status = CALC_ERR;
		}

		return status;
	}

}
