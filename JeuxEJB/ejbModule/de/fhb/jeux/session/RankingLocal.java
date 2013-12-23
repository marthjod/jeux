package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Local;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Local
public interface RankingLocal {

	public List<PlayerDTO> getRankedPlayers(IGroup group);

}
