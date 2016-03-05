package de.fhb.jeux.dto;

import de.fhb.jeux.model.IRanking;

public class RankingDTO {

    private int id;
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
        id = rankingEntity.getPlayer().getId();
        name = rankingEntity.getPlayer().getName();
        groupName = rankingEntity.getGroup().getName();
        points = rankingEntity.getPoints();
        scoreRatio = rankingEntity.getScoreRatio();
        wonGames = rankingEntity.getWonGames();
        rank = rankingEntity.getRank();
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
        sb.append("#");
        sb.append(rank);
        sb.append(" ");
        sb.append(name);
        sb.append(" (");
        sb.append(id);
        sb.append(")");
        sb.append(", group: ");
        sb.append(groupName);
        sb.append(", score ratio ");
        sb.append(scoreRatio);
        sb.append(", won games: ");
        sb.append(wonGames);
        sb.append(", points: ");
        sb.append(points);
        sb.append(">");
        return sb.toString();
    }
}
