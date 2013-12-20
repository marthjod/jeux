package de.fhb.jeux.model;

public interface IPlayer {
	public int getId();

	public IGroup getGroup();

	public String getName();

	public int getPoints();

	public int getScoreRatio();

	public int getRank();

	public boolean equals(IPlayer player);

	public void setPoints(int points);
}
