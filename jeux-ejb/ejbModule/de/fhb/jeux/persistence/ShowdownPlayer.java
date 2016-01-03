package de.fhb.jeux.persistence;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
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
@Table(name = "Player")
@NamedQueries({
    @NamedQuery(name = "Player.findAll", query = "SELECT p FROM ShowdownPlayer p"),
    @NamedQuery(name = "Player.findById", query = "SELECT p FROM ShowdownPlayer p WHERE p.id = :id")})
public class ShowdownPlayer implements IPlayer, Serializable {

    private static final long serialVersionUID = 6766620457612373074L;

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

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private ShowdownGroup group;

    // needed by @Entity
    public ShowdownPlayer() {
    }

    public ShowdownPlayer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // copy constructor
    public ShowdownPlayer(IPlayer player) {
        this.id = player.getId();
        this.name = player.getName();
        this.points = player.getPoints();
        this.scoreRatio = player.getScoreRatio();
        this.rank = player.getRank();
        this.group = (ShowdownGroup) player.getGroup();
    }

    // constructor for converting DTO to Entity
    public ShowdownPlayer(PlayerDTO playerDTO, IGroup group) {
        id = playerDTO.getId();
        name = playerDTO.getName();
        points = playerDTO.getPoints();
        scoreRatio = playerDTO.getScoreRatio();
        rank = playerDTO.getRank();
        this.group = (ShowdownGroup) group;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public int getWonGames() {
        return wonGames;
    }

    @Override
    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    @Override
    public void addWonGame() {
        wonGames++;
    }

    @Override
    public void subtractWonGame() {
        wonGames--;
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
    public boolean equals(IPlayer player) {
        return this.id == player.getId();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(name);
        sb.append(" (");
        sb.append(id);
        sb.append("), group '");
        sb.append(group.getName());
        sb.append("', rank ");
        sb.append(rank);
        sb.append(", points ");
        sb.append(points);
        sb.append(", won games ");
        sb.append(wonGames);
        sb.append(", scoreRatio ");
        sb.append(scoreRatio);
        sb.append(">");

        return sb.toString();
    }
}
