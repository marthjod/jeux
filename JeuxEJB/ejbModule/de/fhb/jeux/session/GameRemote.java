package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;

@Remote
public interface GameRemote {

	public List<GameDTO> getPlayedGameDTOsInGroup(IGroup group);

	public List<IGame> getGamesInGroup(IGroup group);
}
