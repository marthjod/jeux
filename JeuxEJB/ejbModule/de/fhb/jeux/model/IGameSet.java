package de.fhb.jeux.model;

public interface IGameSet {
	public int getId();

	public int getPlayer1Score();

	public int getPlayer2Score();

	public IPlayer getWinner();

	public IGame getGame();

	public boolean equals(IGameSet gameSet);

	public IPlayer getPlayer1();

	public IPlayer getPlayer2();
}
