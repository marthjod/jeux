package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;

@Local
public interface CreatePlayerLocal {
	public boolean createPlayer(PlayerDTO playerDTO, IGroup group);
}
