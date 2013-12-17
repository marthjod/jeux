package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.dto.GameSetDTO;
import de.fhb.jeux.model.IGame;
import de.fhb.jeux.persistence.ShowdownGameSet;

@Stateless
public class UpdateGameBean implements UpdateGameRemote, UpdateGameLocal {

	protected static Logger logger = Logger.getLogger(UpdateGameBean.class);

	@EJB
	private GameDAO gameDAO;

	@EJB
	private PlayerLocal playerBean;

	public UpdateGameBean() {
	}

	@Override
	public boolean updateGame(GameDTO gameDTO) {
		boolean success = false;
		boolean updated = false;
		int i;

		IGame game = gameDAO.getGameById(gameDTO.getId());
		if (game != null && gameDTO != null) {

			logger.debug("Before: " + game);

			List<ShowdownGameSet> existingSets = game.getSets();
			List<GameSetDTO> newSets = gameDTO.getSets();

			// same amount of sets required
			if (existingSets.size() == newSets.size()) {

				// copy scores from DTO to Entity

				// scores aligned?
				if (game.getPlayer1().getId() == gameDTO.getPlayer1Id()
						&& game.getPlayer2().getId() == gameDTO.getPlayer2Id()) {
					for (i = 0; i < existingSets.size(); i++) {
						existingSets.get(i).setPlayer1Score(
								newSets.get(i).getPlayer1Score());
						existingSets.get(i).setPlayer2Score(
								newSets.get(i).getPlayer2Score());

						updated = true;
					}

				} else if (game.getPlayer1().getId() == gameDTO.getPlayer2Id()
						&& game.getPlayer2().getId() == gameDTO.getPlayer1Id()) {

					// distribute values vice versa if not in alignment
					for (i = 0; i < existingSets.size(); i++) {
						existingSets.get(i).setPlayer1Score(
								newSets.get(i).getPlayer2Score());
						existingSets.get(i).setPlayer2Score(
								newSets.get(i).getPlayer1Score());

						updated = true;
					}
				} else {
					logger.warn("Player ID mismatch");
				}

				if (updated) {
					logger.debug("After: " + game);
					// write back
					game.setSets(existingSets);
					gameDAO.updateGame(game);
					success = true;
				} else {
					logger.warn("Unable to update game");
				}
			}
		}

		return success;
	}
}
