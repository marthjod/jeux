package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGame;

public class GameDTO {
	
	private int id;
	private int player1Id;
	private int player2Id;
	private int groupId;
	private int winnerId;

	public GameDTO(){
		
	}
	
	public GameDTO(IGame gameEntity){
		this.id = gameEntity.getId();
		this.player1Id = gameEntity.getPlayer1().getId();
		this.player2Id = gameEntity.getPlayer2().getId();
		this.groupId = gameEntity.getGroup().getId();
		this.winnerId = gameEntity.getWinner().getId();
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append("'" + "' (" + id + ")");
		sb.append(", player1Id: " + player1Id);
		sb.append(", player2Id: " + player2Id);
		sb.append(", group ID: " + groupId);
		sb.append(", winner ID: " + winnerId);
		sb.append(">");
		return sb.toString();
	}
	
}
