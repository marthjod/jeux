package de.fhb.jeux.mockentity;

import de.fhb.jeux.model.IGameSet;
import de.fhb.jeux.util.RandomUtils;

public class MockGameSetEntity implements IGameSet {

	private final int id;
	private final int player1Score;
	private final int player2Score;
	private final int winnerId;
	private final int gameId;
	private final int player1Id;
	private final int player2Id;

	public MockGameSetEntity() {
		this.id = RandomUtils.randInt(1, 12);
		this.player1Score = RandomUtils.randInt(1, 12);
		this.player2Score = RandomUtils.randInt(1, 12);
		this.winnerId = RandomUtils.randInt(1, 12);
		this.gameId = RandomUtils.randInt(1, 12);
		this.player1Id = RandomUtils.randInt(1, 12);
		this.player2Id = this.winnerId;
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
	public int getWinnerId() {
		return winnerId;
	}

	@Override
	public int getGameId() {
		return gameId;
	}

	@Override
	public int getPlayer1Id() {
		return player1Id;
	}

	@Override
	public int getPlayer2Id() {
		return player2Id;
	}

}
