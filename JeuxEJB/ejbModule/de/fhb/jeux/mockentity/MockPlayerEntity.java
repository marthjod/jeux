package de.fhb.jeux.mockentity;

import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.util.RandomUtils;

public class MockPlayerEntity implements IPlayer {

	private final int id;
	private final int groupId;
	private final String name;
	private final int points;
	private final int scoreRatio;
	private final int rank;

	public MockPlayerEntity() {
		this.id = RandomUtils.randInt(1, 12);
		this.groupId = RandomUtils.randInt(1, 6);
		this.name = "Player " + this.id;
		this.points = RandomUtils.randInt(0, 100);
		this.scoreRatio = RandomUtils.randInt(-50, 50);
		this.rank = RandomUtils.randInt(1, 12);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getGroupId() {
		return groupId;
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

}
