package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IGroup;

@Remote
public interface CalcGamesRemote {

	public int writeGamesForGroup(IGroup group, boolean shuffledMode);
}
