package de.fhb.jeux.model;

public interface IPlayer {
	public int getId();

	public IGroup getGroup();

	public void setGroup(IGroup group);

	public String getName();

	public int getPoints();

	public int getScoreRatio();

	public int getRank();

	public boolean equals(IPlayer player);
}
