package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

@Local
public interface AdHocRankingLocal {

	public List<IPlayer> getRankedPlayers(IGroup group);

	public List<PlayerDTO> getRankedPlayerDTOs(IGroup group);

}
