package de.fhb.jeux.dto;

import java.util.ArrayList;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownGameSet;

public class GameDTO {

	private int id;
	private int player1Id;
	private String player1Name;
	private int player2Id;
	private String player2Name;
	private int groupId;
	private String groupName;
	private int winnerId;
	private String winnerName;
	private ArrayList<GameSetDTO> sets;

	public GameDTO() {
	}

	public GameDTO(IGame gameEntity) {
		id = gameEntity.getId();

		// avoid multiple calls to getWinner(), f.ex.
		IPlayer player1 = gameEntity.getPlayer1();
		IPlayer player2 = gameEntity.getPlayer2();
		IGroup group = gameEntity.getGroup();
		// winner may be null
		IPlayer winner = gameEntity.getWinner();

		player1Id = player1.getId();
		player2Id = player2.getId();
		player1Name = player1.getName();
		player2Name = player2.getName();
		groupId = group.getId();
		groupName = group.getName();

		if (winner != null) {
			winnerId = winner.getId();
			winnerName = winner.getName();
		} else {
			winnerId = 0;
			winnerName = "Unknown";
		}

		sets = new ArrayList<GameSetDTO>();
		for (ShowdownGameSet setEntity : gameEntity.getSets()) {
			sets.add(new GameSetDTO(setEntity));
		}
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

	public String getGroupName() {
		return groupName;
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

	public ArrayList<GameSetDTO> getSets() {
		return sets;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i;

		sb.append("<");
		sb.append("ID " + id);
		sb.append(", Player 1: " + player1Name);
		sb.append(" (ID " + player1Id + ")");
		sb.append(", Player 2: " + player2Name);
		sb.append(" (ID " + player2Id + ")");
		sb.append(", group ID: " + groupId);
		sb.append(", Winner: " + winnerName);
		sb.append(" (ID " + winnerId + ")");

		// game sets
		if (sets.size() > 0) {
			sb.append(" [");
			for (i = 0; i < sets.size(); i++) {
				sb.append(sets.get(i) + ", ");
			}
			sb.append("]");
		}

		sb.append(">");
		return sb.toString();
	}
}
