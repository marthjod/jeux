package de.fhb.jeux.dto;

import de.fhb.jeux.model.IPlayer;

public class PlayerDTO {

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

	public void setRank(int rank) {
		this.rank = rank;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append("'" + name + "' (" + id + ")");
		sb.append(", points: " + points);
		sb.append(", score ratio: " + scoreRatio);
		sb.append(", rank: " + rank);
		sb.append(", group: " + groupName);
		sb.append(" (" + groupId + ")");
		sb.append(">");
		return sb.toString();
	}
}