package de.fhb.jeux.model;

public interface IGameSet {
	public int getId();

	public int getPlayer1Score();

	public void setPlayer1Score(int score);

	public int getPlayer2Score();

	public void setPlayer2Score(int score);

	public IPlayer getWinner();

	public void setWinner(IPlayer winner);

	public IGame getGame();

	public boolean equals(IGameSet gameSet);

	public boolean hasWinner();

	public boolean isUnplayed();
	
	public int getNumber();

}
