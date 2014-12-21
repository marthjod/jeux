package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGroup;

@Remote
@SuppressWarnings("ucd")
public interface GameRemote {

	@SuppressWarnings("ucd")
	public List<GameDTO> getPlayedGameDTOsInGroup(IGroup group);

	@SuppressWarnings("ucd")
	public List<GameDTO> getUnplayedGameDTOsInGroup(IGroup group);

}
