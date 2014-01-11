package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.model.IGroup;

@Local
public interface CalcGamesLocal {

	public int writeGamesForGroup(IGroup group, boolean shuffledMode);

}
