package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Remote
public interface CreatePlayerRemote {
	public void createPlayer(PlayerDTO playerDTO, IGroup group);
}
