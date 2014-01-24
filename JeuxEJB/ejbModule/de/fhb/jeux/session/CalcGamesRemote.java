package de.fhb.jeux.session;

import javax.ejb.Remote;

import de.fhb.jeux.model.IGroup;

@Remote
@SuppressWarnings("ucd")
public interface CalcGamesRemote {

	@SuppressWarnings("ucd")
	public int writeGamesForGroup(IGroup group, boolean shuffledMode);
}
