package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.config.BonusPointsDistribution;
import de.fhb.jeux.dto.GameDTO;

@Remote

public interface UpdateGameRemote {
	
	public boolean updateGame(GameDTO gameDTO, BonusPointsDistribution config);
}
