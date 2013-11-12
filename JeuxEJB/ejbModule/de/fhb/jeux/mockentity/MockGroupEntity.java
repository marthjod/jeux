package de.fhb.jeux.mockentity;

import java.util.List;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.util.RandomUtils;

public class MockGroupEntity implements IGroup {

	private int id;
	private String name;
	private int roundId;
	private int minSets;
	private int maxSets;
	private boolean active;
	private boolean completed;
	private List<IPlayer> players;
	private List<IGame> games;

	public MockGroupEntity() {
		this.id = RandomUtils.randInt(1, 1000);
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

	@Override
	public boolean equals(IGroup group) {
		// assuming unique IDs
		return this.id == group.getId();
	}
}
