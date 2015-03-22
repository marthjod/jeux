package de.fhb.jeux.persistence;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "Game")
@NamedQueries({
    @NamedQuery(name = "Game.findAll", query = "SELECT g FROM ShowdownGame g"),
    @NamedQuery(name = "Game.findById", query = "SELECT g FROM ShowdownGame g WHERE g.id = :id"),
    @NamedQuery(name = "Game.findAllInGroup", query = "SELECT g FROM ShowdownGame g WHERE g.group = :group"),
    @NamedQuery(name = "Game.findUnplayedInGroup", query = "SELECT g FROM ShowdownGame g WHERE g.group = :group AND g.winner = null"),
    @NamedQuery(name = "Game.findPlayedInGroup", query = "SELECT g FROM ShowdownGame g WHERE g.group = :group AND g.winner <> null"),
    @NamedQuery(name = "Game.findPlayedByPlayer", query = "SELECT g FROM ShowdownGame g WHERE (g.player1 = :player OR g.player2 = :player) AND g.winner <> null"),
    @NamedQuery(name = "Game.findByContainedPlayers", query = "SELECT g FROM ShowdownGame g WHERE g.group = :group AND ( (g.player1 = :player1 AND g.player2 = :player2) OR (g.player2 = :player1 AND g.player1 = :player2))")})
public class ShowdownGame implements IGame, Serializable {

    private static final long serialVersionUID = -8766860086958636981L;

    public ShowdownGame() {
    }

    public ShowdownGame(IGroup group, IPlayer player1, IPlayer player2) {
        this.group = (ShowdownGroup) group;
        this.player1 = (ShowdownPlayer) player1;
        this.player2 = (ShowdownPlayer) player2;
        this.sets = new ArrayList<ShowdownGameSet>();

        // TODO differentiate maxSets and actual sets #business-logic
        for (int i = 0; i < group.getMaxSets(); i++) {
            this.sets.add(new ShowdownGameSet(this, (i + 1)));
        }
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date playedAt = null;

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
        return winner;
    }

    @Override
    public IPlayer getLoser() {
        IPlayer loser = null;
        if (winner != null) {
            if (winner.equals(player1)) {
                loser = player2;
            } else if (winner.equals(player2)) {
                loser = player1;
            }
        }

        return loser;
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
    public Date getPlayedAt() {
        return playedAt;
    }

    @Override
    public void setPlayedAt(Date playedAt) {
        this.playedAt = playedAt;
    }

    // in some cases, e.g. before inserting a new game,
    // an ID is not set yet, meaning equals() would wrongly return false
    // if we compared IDs as well; thus we cannot consider IDs here
    @Override
    public boolean equals(IGame game) {
        boolean equal = false;

        if (group.equals(game.getGroup())) {
            // p1 == p1' && p2 == p2'
            // -OR-
            // p1 == p2' && p2 == p1'
            if ((player1.equals(game.getPlayer1()) && player2.equals(game
                    .getPlayer2()))
                    || (player1.equals(game.getPlayer2()) && player2
                    .equals(game.getPlayer1()))) {
                equal = true;
            }
        }

        return equal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{ ");
        sb.append("(");
        sb.append(id);
        sb.append(") ");
        sb.append(player1.getName());
        sb.append("--");
        sb.append(player2.getName());
        sb.append(" (");
        sb.append(group.getName());
        sb.append(")");
        if (winner != null) {
            sb.append(" *");
            sb.append(winner.getName());
            sb.append("*");
        }

        // game sets
        if (sets.size() > 0) {
            sb.append(" [ ");
            for (ShowdownGameSet set : sets) {
                sb.append(set);
                sb.append(", ");
            }
            sb.append(" ]");
        }
        sb.append(" }");

        return sb.toString();
    }

    @Override
    public int getSetsWonByPlayer1() {
        return getSetsWonByPlayer(this.player1);
    }

    @Override
    public int getSetsWonByPlayer2() {
        return getSetsWonByPlayer(this.player2);
    }

    @Override
    public int getSetsPlayed() {
        return getSetsWonByPlayer1() + getSetsWonByPlayer2();
    }

    @Override
    public int getSetsPlayedByWinner() {
        int sets = 0;
        if (winner != null) {
            if (winner.equals(player1)) {
                sets = getSetsWonByPlayer1();
            } else if (winner.equals(player2)) {
                sets = getSetsWonByPlayer2();
            }
        }

        return sets;
    }

    private int getSetsWonByPlayer(IPlayer player) {
        int setsWon = 0;
        if (player != null) {
            for (ShowdownGameSet set : sets) {
                if (set.hasWinner() && player.equals(set.getWinner())) {
                    setsWon++;
                }
            }
        }
        return setsWon;
    }
}
