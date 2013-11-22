package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GameSetDTO;
import de.fhb.jeux.model.IGameSet;

@Remote
public interface GameSetRemote {

	public List<IGameSet> getAllGameSets();

	public List<GameSetDTO> getAllGameSetDTOs();
}
