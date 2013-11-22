package de.fhb.jeux.dto;


import de.fhb.jeux.model.IGameSet;

public class GameSetDTO {
	
	public GameSetDTO () {
		
	}
	
	private int id;
	private int gameId;
	private int player1Id;
	private int player2Id;
	private int winnerId;
	private String winnerName;
	private String player1Name;
	private String player2Name;
	
	public GameSetDTO (IGameSet iGameSetEntity) {
		this.id = iGameSetEntity.getId();
		this.gameId = iGameSetEntity.getGame().getId();
		this.player1Id = iGameSetEntity.getPlayer1().getId();
		this.player2Id = iGameSetEntity.getPlayer2().getId();
		this.winnerId  = iGameSetEntity.getWinner().getId();
		this.player1Name = iGameSetEntity.getPlayer1().getName();
		this.player2Name = iGameSetEntity.getPlayer2().getName();
		this.winnerName = iGameSetEntity.getWinner().getName();
	}

	public int getId() {
		return id;
	}

	public int getGameId() {
		return gameId;
	}

	public int getPlayer1Id() {
		return player1Id;
	}

	public int getPlayer2Id() {
		return player2Id;
	}

	public int getWinnerId() {
		return winnerId;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public String getPlayer1Name() {
		return player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append("'" + "' (" + id + ")");
		sb.append(", Player1: " + player1Name);
		sb.append(", Id: " + player1Id);
		sb.append(", Player2: " + player2Name);
		sb.append(", Id: " + player2Id);
		sb.append(", game ID: " + gameId);
		sb.append(", Winner: " + winnerName);
		sb.append(", winner ID: " + winnerId);
		sb.append(">");
		return sb.toString();
	}
	
	

}