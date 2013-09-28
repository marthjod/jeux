package de.fhb.jeux.mockentity;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.util.RandomUtils;

public class MockGameEntity implements IGame {

	private final int id;
	private final int groupId;
	private final int winnerId;

	public MockGameEntity() {
		this.id = RandomUtils.randInt(1, 12);
		this.groupId = RandomUtils.randInt(1, 6);
		this.winnerId = RandomUtils.randInt(1, 50);
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
	public int getWinnerId() {
		return winnerId;
	}
}
