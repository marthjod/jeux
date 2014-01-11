package de.fhb.jeux.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jboss.logging.Logger;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

@Entity
@Table(name = "Game")
@NamedQueries({
		@NamedQuery(name = "Game.findAll", query = "SELECT g FROM ShowdownGame g"),
		@NamedQuery(name = "Game.findById", query = "SELECT g FROM ShowdownGame g WHERE g.id = :id"),
		@NamedQuery(name = "Game.findAllInGroup", query = "SELECT g FROM ShowdownGame g WHERE g.group = :group"),
		@NamedQuery(name = "Game.findUnplayedInGroup", query = "SELECT g FROM ShowdownGame g WHERE g.group = :group AND g.winner = null"),
		@NamedQuery(name = "Game.findPlayedInGroup", query = "SELECT g FROM ShowdownGame g WHERE g.group = :group AND g.winner <> null") })
public class ShowdownGame implements IGame, Serializable {

	private static final long serialVersionUID = -8766860086958636981L;
	protected static Logger logger = Logger.getLogger(ShowdownGame.class);

	public ShowdownGame() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "groupId")
	private ShowdownGroup group;

	@OneToOne
	@JoinColumn(name = "player1Id")
	private ShowdownPlayer player1;

	@OneToOne
	@JoinColumn(name = "player2Id")
	private ShowdownPlayer player2;

	@OneToOne
	@JoinColumn(name = "winnerId")
	private ShowdownPlayer winner;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "game")
	private List<ShowdownGameSet> sets;

	@Transient
	private int player1Score;

	@Transient
	private int player2Score;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public IGroup getGroup() {
		return group;
	}

	@Override
	public IPlayer getPlayer1() {
		return player1;
	}

	@Override
	public IPlayer getPlayer2() {
		return player2;
	}

	// @Override
	// public IPlayer getWinner() {
	// int player1WonSets = 0;
	// int player2WonSets = 0;
	// IPlayer winner;
	//
	// // do this here so we loop only once
	// for (IGameSet set : sets) {
	// if (player1.equals(set.getWinner())) {
	// logger.debug(player1.getName() + " (player 1) has won set "
	// + set.getPlayer1Score() + ":" + set.getPlayer2Score());
	// player1WonSets++;
	// } else if (player2.equals(set.getWinner())) {
	// logger.debug(player2.getName() + " (player 2) has won set "
	// + set.getPlayer2Score() + ":" + set.getPlayer1Score());
	// player2WonSets++;
	// } else {
	// logger.warn("Unknown player in set");
	// }
	// }
	//
	// logger.debug(player1.getName() + " : " + player2.getName() + " "
	// + player1WonSets + ":" + player2WonSets);
	//
	// if (player1WonSets > player2WonSets) {
	// winner = player1;
	// } else if (player2WonSets > player1WonSets) {
	// winner = player2;
	// } else {
	// // same amount of won sets
	// winner = null;
	// }
	//
	// if (winner != null) {
	// logger.debug("Winner: " + winner.getName());
	// } else {
	// logger.warn("Unable to determine a winner");
	// }
	//
	// return winner;
	// }

	@Override
	public IPlayer getWinner() {
		return winner;
	}

	@Override
	public boolean hasWinner() {
		return winner != null;
	}

	@Override
	public void setWinner(IPlayer winner) {
		// this.
		this.winner = (ShowdownPlayer) winner;
	}

	@Override
	public List<ShowdownGameSet> getSets() {
		return sets;
	}

	@Override
	public void setSets(List<ShowdownGameSet> sets) {
		// this.
		this.sets = sets;
	}

	@Override
	public boolean equals(IGame game) {
		// assuming unique IDs
		return id == game.getId();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("(" + id + ") ");
		sb.append(player1.getName());
		sb.append("--" + player2.getName());
		sb.append(" (" + group.getName() + ")");
		if (winner != null) {
			sb.append(" *" + winner.getName() + "*");
		}

		// game sets
		if (sets.size() > 0) {
			sb.append(" [ ");
			for (int i = 0; i < sets.size(); i++) {
				sb.append(sets.get(i) + ", ");
			}
			sb.append(" ]");
		}
		sb.append(" }");
		return sb.toString();
	}
}
