package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGameSet;

public class GameSetDTO implements Comparable {

    public GameSetDTO() {
    }

    private int id;
    private int gameId;
    private int winnerId;
    private int player1Score;
    private int player2Score;
    private String winnerName;
    private int number;

    // EJB business method must be public
    public GameSetDTO(IGameSet gameSetEntity) {
        id = gameSetEntity.getId();
        gameId = gameSetEntity.getGame().getId();
        player1Score = gameSetEntity.getPlayer1Score();
        player2Score = gameSetEntity.getPlayer2Score();
        number = gameSetEntity.getNumber();

        if (gameSetEntity.getWinner() != null) {
            winnerId = gameSetEntity.getWinner().getId();
            winnerName = gameSetEntity.getWinner().getName();
        } else {
            winnerId = 0;
            winnerName = "Unknown";
        }
    }

    public GameSetDTO(int id, int gameId, int winnerId, int player1Score,
            int player2Score, String winnerName, int number) {
        this.id = id;
        this.gameId = gameId;
        this.winnerId = winnerId;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.winnerName = winnerName;
        this.number = number;
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

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append("ID ").append(id);
        sb.append(", game ID: ").append(gameId);
        sb.append(", player 1 score: ").append(player1Score);
        sb.append(", player 2 score: ").append(player2Score);
        sb.append(", winner: ").append(winnerName);
        sb.append(", set # ").append(number);
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

    @Override
    public int compareTo(Object set) {
        return this.number - ((GameSetDTO) set).getNumber();
    }
}
