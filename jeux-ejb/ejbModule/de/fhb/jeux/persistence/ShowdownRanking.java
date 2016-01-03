package de.fhb.jeux.persistence;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.model.IRanking;
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
import javax.persistence.Table;

@Entity
@Table(name = "Ranking")
@NamedQueries({
    @NamedQuery(name = "Ranking.findByGroup", query = "SELECT r FROM ShowdownRanking r WHERE r.group = :group ORDER BY rank")})
public class ShowdownRanking implements IRanking, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int points;

    @Column
    private int scoreRatio;

    @Column
    private int rank;

    @Column
    private int wonGames;

    @ManyToOne
    @JoinColumn(name = "playerId")
    private ShowdownPlayer player;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private ShowdownGroup group;

    // needed by @Entity
    public ShowdownRanking() {
    }

    public ShowdownRanking(IPlayer player, IGroup group) {
        this.group = (ShowdownGroup) group;
        this.player = (ShowdownPlayer) player;
        this.points = this.player.getPoints();
        this.rank = this.player.getRank();
        this.scoreRatio = this.player.getScoreRatio();
        this.wonGames = this.player.getWonGames();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public IGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(IGroup group) {
        this.group = (ShowdownGroup) group;
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(IPlayer player) {
        this.player = (ShowdownPlayer) player;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int getScoreRatio() {
        return scoreRatio;
    }

    @Override
    public void setScoreRatio(int scoreRatio) {
        this.scoreRatio = scoreRatio;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int getWonGames() {
        return wonGames;
    }

    @Override
    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append("#" + rank + " ");
        sb.append(player.getName());
        sb.append(" (" + id + ")");
        sb.append(", group: " + group.getName());
        sb.append(", score ratio " + scoreRatio);
        sb.append(", won games: " + wonGames);
        sb.append(", points: " + points);
        sb.append(">");
        return sb.toString();
    }

}
