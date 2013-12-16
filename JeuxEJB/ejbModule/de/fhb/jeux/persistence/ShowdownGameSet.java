package de.fhb.jeux.persistence;

import java.io.Serializable;

import javax.persistence.Column;
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
@Table(name = "GameSet")
@NamedQueries({
		@NamedQuery(name = "GameSet.findAll", query = "SELECT gs FROM ShowdownGameSet gs"),
		@NamedQuery(name = "GameSet.findById", query = "SELECT gs FROM ShowdownGameSet gs WHERE gs.id = :id") })
public class ShowdownGameSet implements IGameSet, Serializable {

	private static final long serialVersionUID = 6276609884514398233L;
	protected static Logger logger = Logger.getLogger(ShowdownGameSet.class);

	public ShowdownGameSet() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private int player1Score;

	@Column
	private int player2Score;

	@ManyToOne
	@JoinColumn(name = "gameId")
	private ShowdownGame game;

	@OneToOne
	@JoinColumn(name = "winnerId")
	private ShowdownPlayer winner;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public IPlayer getWinner() {
		return player1Score > player2Score ? game.getPlayer1() : game
				.getPlayer2();
	}

	@Override
	public int getPlayer1Score() {
		return player1Score;
	}

	@Override
	public int getPlayer2Score() {
		return player2Score;
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
