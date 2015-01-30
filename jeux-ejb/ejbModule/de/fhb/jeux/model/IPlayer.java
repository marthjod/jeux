package de.fhb.jeux.model;

public interface IPlayer {
	public int getId();

	public IGroup getGroup();

	public void setGroup(IGroup group);

	public String getName();

	public int getPoints();

	public int getScoreRatio();

	public int getRank();

	public void setRank(int rank);

	public boolean equals(IPlayer player);

	public void setPoints(int points);

	public void setScoreRatio(int scoreRatio);

	public int getWonGames();

	public void setWonGames(int wonGames);

	public void addWonGame();

	public void subtractWonGame();
}