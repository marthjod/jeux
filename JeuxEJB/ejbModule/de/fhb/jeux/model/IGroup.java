package de.fhb.jeux.model;

import java.util.List;

import de.fhb.jeux.persistence.ShowdownPlayer;

public interface IGroup {
	public int getId();

	public String getName();

	public int getRoundId();

	public int getMinSets();

	public int getMaxSets();

	public List<ShowdownPlayer> getPlayers();

	public boolean isActive();

	public boolean isCompleted();

	public boolean equals(IGroup group);
}
