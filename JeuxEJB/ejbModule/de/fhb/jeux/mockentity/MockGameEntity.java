package de.fhb.jeux.mockentity;

import java.util.List;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGameSet;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

public class MockGameEntity implements IGame {

	private int id;
	private IGroup group;
	private IPlayer winner;
	private IPlayer player1;
	private IPlayer player2;

	private List<IGameSet> sets;

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
	public IGroup getGroup() {
		return this.group;
	}

	@Override
	public IPlayer getWinner() {
		// may be null at first
		return this.winner;
	}

	@Override
	public List<IGameSet> getSets() {
		return sets;
	}

	@Override
	public boolean equals(IGame game) {
		// assuming unique IDs
		return this.id == game.getId();
	}

	@Override
	public IPlayer getPlayer1() {
		return player1;
	}

	@Override
	public IPlayer getPlayer2() {
		return player2;
	}
}
