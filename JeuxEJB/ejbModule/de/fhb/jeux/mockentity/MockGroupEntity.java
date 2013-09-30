package de.fhb.jeux.mockentity;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.util.RandomUtils;

public class MockGroupEntity implements IGroup {

	private final int id;
	private final String name;
	private final int roundId;
	private final int minSets;
	private final int maxSets;
	private final boolean active;
	private final boolean completed;

	public MockGroupEntity() {
		this.id = RandomUtils.randInt(1, 12);
		this.name = "Group " + this.id;
		this.roundId = RandomUtils.randInt(1, 5);
		this.minSets = 1;
		this.maxSets = 1;
		this.active = true;
		this.completed = false;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getRoundId() {
		return roundId;
	}

	@Override
	public int getMinSets() {
		return minSets;
	}

	@Override
	public int getMaxSets() {
		return maxSets;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}

	@Override
	public String toString() {
		return "'" + this.name + "' (ID " + this.id + "), " + this.minSets
				+ "-" + this.maxSets + " sets";
	}
}
