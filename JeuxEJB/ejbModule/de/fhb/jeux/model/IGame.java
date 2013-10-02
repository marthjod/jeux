package de.fhb.jeux.model;

import java.util.List;

public interface IGame {
	public int getId();

	public IGroup getGroup();

	public IPlayer getPlayer1();

	public IPlayer getPlayer2();

	public IPlayer getWinner();

	public List<IGameSet> getSets();

	public boolean equals(IGame game);
}