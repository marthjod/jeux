package de.fhb.jeux.dto;

import de.fhb.jeux.model.IPlayer;

public class PlayerDTO implements Comparable {

    private int id;
    private int points;
    private int scoreRatio;
    private int wonGames;
    private int rank;
    private String name;
    private int groupId;
    private String groupName;

    // argument-less constructor for converter libs
    public PlayerDTO() {
    }

    // parametrized constructor for conversion from interface implementations
    // (i.e. Persistent Entities)
    public PlayerDTO(IPlayer playerEntity) {
        this.id = playerEntity.getId();
        this.points = playerEntity.getPoints();
        this.scoreRatio = playerEntity.getScoreRatio();
        this.rank = playerEntity.getRank();
        this.wonGames = playerEntity.getWonGames();
        this.name = playerEntity.getName();
        this.groupId = playerEntity.getGroup().getId();
        this.groupName = playerEntity.getGroup().getName();
    }

    public PlayerDTO(String name) {
        this.name = name;
    }

    public PlayerDTO(int id, String name, int points, int scoreRatio,
            int wonGames, int rank, int groupId, String groupName) {
        this.id = id;
        this.points = points;
        this.scoreRatio = scoreRatio;
        this.wonGames = wonGames;
        this.rank = rank;
        this.name = name;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public int getScoreRatio() {
        return scoreRatio;
    }

    public int getRank() {
        return rank;
    }

    public int getWonGames() {
        return wonGames;
    }

    public String getName() {
        return name;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append("'").append(name).append("' (").append(id).append(")");
        sb.append(", points: ").append(points);
        sb.append(", score ratio: ").append(scoreRatio);
        sb.append(", rank: ").append(rank);
        sb.append(", group: ").append(groupName);
        sb.append(" (").append(groupId).append(")");
        sb.append(">");
        return sb.toString();
    }

    @Override
    public int compareTo(Object player) {
        return this.name.compareTo(((PlayerDTO) player).getName());
    }
}
