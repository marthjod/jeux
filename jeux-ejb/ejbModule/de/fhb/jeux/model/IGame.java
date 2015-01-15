package de.fhb.jeux.model;

import java.util.List;

import de.fhb.jeux.persistence.ShowdownGameSet;

public interface IGame {
	public int getId();

	public IGroup getGroup();

	public IPlayer getPlayer1();

	public IPlayer getPlayer2();

	public IPlayer getWinner();

	public IPlayer getLoser();

	public List<ShowdownGameSet> getSets();

	public boolean equals(IGame game);

	public void setSets(List<ShowdownGameSet> sets);

	public void setWinner(IPlayer winner);

	public boolean hasWinner();

	public int getSetsWonByPlayer1();

	public int getSetsWonByPlayer2();

	public int getSetsPlayed();

	public int getSetsPlayedByWinner();
}