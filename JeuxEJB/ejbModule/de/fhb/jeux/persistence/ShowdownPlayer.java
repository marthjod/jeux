package de.fhb.jeux.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

@Entity
@NamedQueries({
		@NamedQuery(name = "Player.findAll", query = "SELECT g FROM ShowdownPlayer g"),
		@NamedQuery(name = "Player.findById", query = "SELECT g FROM ShowdownPlayer g WHERE g.id = :id") })


public class ShowdownPlayer implements IPlayer, Serializable{

	private static final long serialVersionUID = 6766620457612373074L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private int groupId;
	
	@Column
	private int points;
	
	@Column
	private int scoreRatio;
	
	@Column
	private int rank;
	
	@Column
	private String name;
	
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public IGroup getGroup() {
		return null;
	}
	
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Override
	public void setGroup(IGroup group) {
	}
	

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public int getScoreRatio() {
		return scoreRatio;
	}
	
	public void setScoreRatio(int scoreRatio) {
		this.scoreRatio = scoreRatio;
	}

	@Override
	public int getRank() {
		return rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public boolean equals(IPlayer player) {
		return false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" Rank: ");
		sb.append(this.rank);
		sb.append("' (ID ");
		sb.append(this.id);
		sb.append("), groupId ");
		sb.append(this.groupId);
		sb.append(", name = ");
		sb.append(this.name);
		sb.append(", points ");
		sb.append(this.points);
		sb.append(", scoreRatio ");
		sb.append(this.scoreRatio);
		sb.append("-");

		return sb.toString();
	}

}
