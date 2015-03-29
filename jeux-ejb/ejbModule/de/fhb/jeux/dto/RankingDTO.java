package de.fhb.jeux.dto;

import de.fhb.jeux.model.IRanking;

public class RankingDTO {

    // == player ID
    private int id;
    // == player name
    private String name;
    private String groupName;
    private int points;
    private int scoreRatio;
    private int rank;
    private int wonGames;

    // argument-less constructor for converter libs
    public RankingDTO() {
    }

    // parametrized constructor for conversion from interface implementations
    // (i.e. Persistent Entities)
    public RankingDTO(IRanking rankingEntity) {
        this.id = rankingEntity.getPlayer().getId();
        this.name = rankingEntity.getPlayer().getName();
        this.groupName = rankingEntity.getGroup().getName();
        this.points = rankingEntity.getPoints();
        this.scoreRatio = rankingEntity.getScoreRatio();
        this.wonGames = rankingEntity.getWonGames();
        this.rank = rankingEntity.getRank();
    }

    // getters necessary or not added to DTO in template otherwise!
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getScoreRatio() {
        return scoreRatio;
    }

    public void setScoreRatio(int scoreRatio) {
        this.scoreRatio = scoreRatio;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append("#" + rank + " ");
        sb.append(name);
        sb.append(" (" + id + ")");
        sb.append(", group: " + groupName);
        sb.append(", score ratio " + scoreRatio);
        sb.append(", won games: " + wonGames);
        sb.append(", points: " + points);
        sb.append(">");
        return sb.toString();
    }
}
