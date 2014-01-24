package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Remote
@SuppressWarnings("ucd")
public interface CreatePlayerRemote {

	@SuppressWarnings("ucd")
	public void createPlayer(PlayerDTO playerDTO, IGroup group);
}
