package de.fhb.jeux.model;

public interface IPlayer {

    public int getId();

    public IGroup getGroup();

    public void setGroup(IGroup group);

    public String getName();

    public int getPoints();

    public int getScore();

    public void setScore(int score);

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

    public void setName(String name);

    public void resetStats();

    public int getWonSets();

    public void setWonSets(int wonSets);

    public int getLostSets();

    public void setLostSets(int lostSets);

}
