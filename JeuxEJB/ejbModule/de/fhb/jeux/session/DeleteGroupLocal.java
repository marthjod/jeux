package de.fhb.jeux.session;

import javax.ejb.Local;

import de.fhb.jeux.model.IGroup;

@Local
public interface DeleteGroupLocal {
	public int deleteGroup(IGroup group);
}
