package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGameSet;

public class GameSetDTO {

	public GameSetDTO() {
	}

	private int id;
	private int gameId;
	private int winnerId;
	private int player1Score;
	private int player2Score;
	private String winnerName;

	public GameSetDTO(IGameSet gameSetEntity) {
		id = gameSetEntity.getId();
		gameId = gameSetEntity.getGame().getId();
		player1Score = gameSetEntity.getPlayer1Score();
		player2Score = gameSetEntity.getPlayer2Score();
		winnerId = gameSetEntity.getWinner().getId();
		winnerName = gameSetEntity.getWinner().getName();
	}

	public int getId() {
		return id;
	}

	public int getGameId() {
		return gameId;
	}

	public int getWinnerId() {
		return winnerId;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public int getPlayer1Score() {
		return player1Score;
	}

	public int getPlayer2Score() {
		return player2Score;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append("'" + "' (" + id + ")");
		sb.append(", game ID: " + gameId);
		sb.append(", Winner: " + winnerName);
		sb.append(", winner ID: " + winnerId);
		sb.append(">");
		return sb.toString();
	}

}
