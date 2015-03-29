package de.fhb.jeux.model;

public interface IRanking {

    public int getId();

    public IGroup getGroup();

    public void setGroup(IGroup group);

    public IPlayer getPlayer();

    public void setPlayer(IPlayer player);

    public int getPoints();

    public void setPoints(int points);

    public int getScoreRatio();

    public void setScoreRatio(int scoreRatio);

    public int getRank();

    public void setRank(int rank);

    public int getWonGames();

    public void setWonGames(int wonGames);
}
