package de.fhb.jeux.model;

public interface IGroup {
	public int getId();

	public String getName();

	public int getRoundId();

	public int getMinSets();

	public int getMaxSets();

	public boolean isActive();

	public boolean isCompleted();

	@Override
	public String toString();
}
