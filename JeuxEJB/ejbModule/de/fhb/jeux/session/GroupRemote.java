package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.Remote;

import de.fhb.jeux.model.IGroup;

@Remote
public interface GroupRemote {
	public List<IGroup> getAllGroups();
}
