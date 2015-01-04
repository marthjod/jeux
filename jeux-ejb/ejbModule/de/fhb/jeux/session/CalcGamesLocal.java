package de.fhb.jeux.session;

import javax.ejb.Local;
import javax.servlet.ServletContext;

import de.fhb.jeux.model.IGroup;

@Local
public interface CalcGamesLocal {

	public int writeGamesForGroup(IGroup group, boolean shuffledMode);

	public String getShuffledGamesList(IGroup group, String format,
			ServletContext sc);

}
