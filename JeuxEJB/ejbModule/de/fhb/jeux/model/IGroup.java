package de.fhb.jeux.model;

import java.util.List;

public interface IGroup {
	public int getId();

	public String getName();

	public int getRoundId();

	public int getMinSets();

	public int getMaxSets();

	public boolean isActive();

	public boolean isCompleted();

	public List<IPlayer> getPlayers();

	public List<IGame> getGames();

	public boolean equals(IGroup group);
}
