package de.fhb.jeux.persistence;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGameSet;
import de.fhb.jeux.model.IPlayer;

@Entity
@Table(name = "Gameset")
@NamedQueries({
		@NamedQuery(name = "GameSet.findAll", query = "SELECT g FROM ShowdownGameSet g"),
		@NamedQuery(name = "GameSet.findById", query = "SELECT g FROM ShowdownGameSet g WHERE g.id = :id") })
public class ShowdownGameSet implements IGameSet, Serializable {

	private static final long serialVersionUID = 6276609884514398233L;
	protected static Logger logger = Logger.getLogger(ShowdownGameSet.class);

	public ShowdownGameSet() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "gameId")
	private ShowdownGame game;

	@OneToOne
	@JoinColumn(name = "player1Id")
	private ShowdownPlayer player1;

	@OneToOne
	@JoinColumn(name = "player2Id")
	private ShowdownPlayer player2;

	@OneToOne
	@JoinColumn(name = "winnerId")
	private ShowdownPlayer winner;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public IPlayer getPlayer1() {
		return player1;
	}

	@Override
	public IPlayer getPlayer2() {
		return player2;
	}

	@Override
	public IPlayer getWinner() {
		return player1.getPoints() > player2.getPoints() ? player1 : player2;
	}

	@Override
	public int getPlayer1Score() {
		return player1.getScoreRatio();
	}

	@Override
	public int getPlayer2Score() {
		return player2.getScoreRatio();
	}

	@Override
	public IGame getGame() {
		return game;
	}

	@Override
	public boolean equals(IGameSet gameSet) {
		// assuming unique IDs
		return this.id == gameSet.getId();
	}

}
