package de.fhb.jeux.dto;

import java.util.ArrayList;
import java.util.Date;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.persistence.ShowdownGameSet;

public class GameDTO implements Comparable {

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
    private Date playedAt;

    public GameDTO() {
    }

    public GameDTO(IGame gameEntity) {
        id = gameEntity.getId();
        player1Id = gameEntity.getPlayer1().getId();
        player2Id = gameEntity.getPlayer2().getId();
        player1Name = gameEntity.getPlayer1().getName();
        player2Name = gameEntity.getPlayer2().getName();
        groupId = gameEntity.getGroup().getId();
        groupName = gameEntity.getGroup().getName();
        playedAt = gameEntity.getPlayedAt();

        if (gameEntity.getWinner() != null) {
            winnerId = gameEntity.getWinner().getId();
            winnerName = gameEntity.getWinner().getName();
        } else {
            winnerId = 0;
            winnerName = "Unknown";
        }

        sets = new ArrayList<GameSetDTO>();
        for (ShowdownGameSet setEntity : gameEntity.getSets()) {
            sets.add(new GameSetDTO(setEntity));
        }
    }

    // copy constructor
    public GameDTO(GameDTO dto) {
        id = dto.getId();
        player1Id = dto.getPlayer1Id();
        player1Name = dto.getPlayer1Name();
        player2Id = dto.getPlayer2Id();
        player2Name = dto.getPlayer2Name();
        groupId = dto.getGroupId();
        groupName = dto.getGroupName();
        winnerId = dto.getWinnerId();
        winnerName = dto.getWinnerName();
        sets = dto.getSets();
        playedAt = dto.getPlayedAt();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public void setSets(ArrayList<GameSetDTO> sets) {
        this.sets = sets;
    }

    public void setPlayedAt(Date playedAt) {
        this.playedAt = playedAt;
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

    public Date getPlayedAt() {
        return playedAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i;

        sb.append("<");
        sb.append("ID ").append(id);
        sb.append(", Player 1: ").append(player1Name);
        sb.append(" (ID ").append(player1Id).append(")");
        sb.append(", Player 2: ").append(player2Name);
        sb.append(" (ID ").append(player2Id).append(")");
        sb.append(", group ID: ").append(groupId);
        sb.append(", Winner: ").append(winnerName);
        sb.append(" (ID ").append(winnerId).append(")");

        // game sets
        if (sets.size() > 0) {
            sb.append(" [");
            for (i = 0; i < sets.size(); i++) {
                sb.append(sets.get(i)).append(", ");
            }
            sb.append("]");
        }

        sb.append(">");
        return sb.toString();
    }

    @Override
    public int compareTo(Object game) {
        return this.playedAt == null ? 0 : this.playedAt.compareTo(((GameDTO) game).getPlayedAt());
    }
}
