package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.config.BonusPointsDistribution;
import de.fhb.jeux.dto.GameDTO;

@Local
public interface UpdateGameLocal {
	public boolean updateGame(GameDTO gameDTO, BonusPointsDistribution config);
}
