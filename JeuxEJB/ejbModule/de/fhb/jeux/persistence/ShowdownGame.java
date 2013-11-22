package de.fhb.jeux.persistence;

import java.io.Serializable;
import java.util.List;

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
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

@Entity
@Table(name = "Game")
@NamedQueries({
		@NamedQuery(name = "Game.findAll", query = "SELECT g FROM ShowdownGame g"),
		@NamedQuery(name = "Game.findById", query = "SELECT g FROM ShowdownGame g WHERE g.id = :id") })
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

	@Override
	public IPlayer getWinner() {
		return player1.getPoints() > player2.getPoints() ? player1 : player2;
	}

	@Override
	public List<IGameSet> getSets() {
		return null;
	}

	@Override
	public boolean equals(IGame game) {
		// TODO
		return false;
	}

}
