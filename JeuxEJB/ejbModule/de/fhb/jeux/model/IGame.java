package de.fhb.jeux.model;

import java.util.List;

import de.fhb.jeux.persistence.ShowdownGameSet;

public interface IGame {
	public int getId();

	public IGroup getGroup();

	public IPlayer getPlayer1();

	public IPlayer getPlayer2();

	public IPlayer getWinner();

	public List<ShowdownGameSet> getSets();

	public boolean equals(IGame game);
}