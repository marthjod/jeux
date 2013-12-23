package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Remote
public interface RankingRemote {
	public List<PlayerDTO> getRankedPlayers(IGroup group);
}