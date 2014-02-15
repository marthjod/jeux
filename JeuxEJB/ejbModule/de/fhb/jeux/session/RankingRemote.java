package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

@Remote
@SuppressWarnings("ucd")
public interface RankingRemote {

	public List<IPlayer> getRankedPlayers(IGroup group);

	@SuppressWarnings("ucd")
	public List<PlayerDTO> getRankedPlayerDTOs(IGroup group);
}
