package de.fhb.jeux.mockentity;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

public class MockPlayerEntity implements IPlayer {

	private int id;
	private IGroup group;
	private String name;
	private int points;
	private int scoreRatio;
	private int rank;

	public MockPlayerEntity() {
		// this.id = RandomUtils.randInt(1, 1000);
		// this.group = new MockGroupEntity();
		// this.name = "Player " + this.id;
		// this.points = RandomUtils.randInt(0, 100);
		// this.scoreRatio = RandomUtils.randInt(-50, 50);
		// this.rank = RandomUtils.randInt(1, 12);
	}

	public MockPlayerEntity(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public IGroup getGroup() {
		return this.group;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPoints() {
		return points;
	}

	@Override
	public int getScoreRatio() {
		return scoreRatio;
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public boolean equals(IPlayer player) {
		// assuming unique IDs
		return this.id == player.getId();
	}

	@Override
	public String toString() {
		return "Player " + this.name;
	}

	@Override
	public void setGroup(IGroup group) {
		this.group = group;
	}

}
