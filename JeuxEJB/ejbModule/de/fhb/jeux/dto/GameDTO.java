package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGame;

public class GameDTO {
	
	private int id;
	private int player1Id;
	private String player1Name;
	private int player2Id;
	private String player2Name;
	private int groupId;
	private int winnerId;
	private String winnerName;

	public GameDTO(){
		
	}
	
	public GameDTO(IGame gameEntity){
		this.id = gameEntity.getId();
		this.player1Id = gameEntity.getPlayer1().getId();
		this.player2Id = gameEntity.getPlayer2().getId();
		this.player1Name = gameEntity.getPlayer1().getName();
		this.player2Name = gameEntity.getPlayer2().getName();
		this.groupId = gameEntity.getGroup().getId();
		this.winnerId = gameEntity.getWinner().getId();
		this.winnerName = gameEntity.getWinner().getName();
	}

	public int getId() {
		return id;
	}

	public int getPlayer1Id() {
		return player1Id;
	}

	public int getPlayer2Id() {
		return player2Id;
	}

	public int getGroupId() {
		return groupId;
	}

	public int getWinnerId() {
		return winnerId;
	}
	
	public String getPlayer1Name() {
		return player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public String getWinnerName() {
		return winnerName;
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
		sb.append(", group ID: " + groupId);
		sb.append(", Winner: " + winnerName);
		sb.append(", winner ID: " + winnerId);
		sb.append(">");
		return sb.toString();
	}
	
}
