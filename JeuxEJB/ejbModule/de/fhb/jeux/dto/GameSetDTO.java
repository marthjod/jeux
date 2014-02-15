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

	// EJB business method must be public
	@SuppressWarnings("ucd")
	public GameSetDTO(IGameSet gameSetEntity) {
		id = gameSetEntity.getId();
		gameId = gameSetEntity.getGame().getId();
		player1Score = gameSetEntity.getPlayer1Score();
		player2Score = gameSetEntity.getPlayer2Score();

		if (gameSetEntity.getWinner() != null) {
			winnerId = gameSetEntity.getWinner().getId();
			winnerName = gameSetEntity.getWinner().getName();
		} else {
			winnerId = 0;
			winnerName = "Unknown";
		}
	}

	public GameSetDTO(int id, int gameId, int winnerId, int player1Score,
			int player2Score, String winnerName) {
		this.id = id;
		this.gameId = gameId;
		this.winnerId = winnerId;
		this.player1Score = player1Score;
		this.player2Score = player2Score;
		this.winnerName = winnerName;
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
		sb.append("ID " + id);
		sb.append(", game ID: " + gameId);
		sb.append(", player 1 score: " + player1Score);
		sb.append(", player 2 score: " + player2Score);
		sb.append(", winner: " + winnerName);
		sb.append(" (ID " + winnerId + ")");
		sb.append(">");
		return sb.toString();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setWinnerId(int winnerId) {
		this.winnerId = winnerId;
	}

	public void setPlayer1Score(int player1Score) {
		this.player1Score = player1Score;
	}

	public void setPlayer2Score(int player2Score) {
		this.player2Score = player2Score;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}
}
