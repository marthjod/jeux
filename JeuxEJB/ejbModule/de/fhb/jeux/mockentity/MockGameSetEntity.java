package de.fhb.jeux.mockentity;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGameSet;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.util.RandomUtils;

public class MockGameSetEntity implements IGameSet {

	private int id;
	private int player1Score;
	private int player2Score;
	private IPlayer winner;
	private IGame game;

	public MockGameSetEntity() {
		this.id = RandomUtils.randInt(1, 1000);
		this.player1Score = RandomUtils.randInt(1, 12);
		this.player2Score = RandomUtils.randInt(1, 12);
		this.winner = new MockPlayerEntity();
		this.game = new MockGameEntity();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getPlayer1Score() {
		return player1Score;
	}

	@Override
	public int getPlayer2Score() {
		return player2Score;
	}

	@Override
	public IPlayer getWinner() {
		return this.winner;
	}

	@Override
	public IGame getGame() {
		return this.game;
	}

	@Override
	public boolean equals(IGameSet gameSet) {
		// assuming unique IDs
		return this.id == gameSet.getId();
	}
}
