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
    private int score;

    @Column
    private int scoreRatio;

    @Column
    private int rank;

    @Column
    private int wonGames;

    @Column
    private int wonSets;

    @Column
    private int lostSets;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private ShowdownGroup group;

    // needed by @Entity
    public ShowdownPlayer() {
    }

    public ShowdownPlayer(int id, String name, int points, int scoreRatio, int rank,
            int wonGames, ShowdownGroup group) {
        this.id = id;
        this.points = points;
        this.scoreRatio = scoreRatio;
        this.rank = rank;
        this.wonGames = wonGames;
        this.name = name;
        this.group = group;
    }

    // constructor for converting DTO to Entity
    public ShowdownPlayer(PlayerDTO playerDTO, IGroup group) {
        this(playerDTO.getId(), playerDTO.getName(), playerDTO.getPoints(),
                playerDTO.getScoreRatio(), playerDTO.getRank(),
                playerDTO.getWonGames(), (ShowdownGroup) group);
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
    public int getScore() {
        return score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
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
    public void resetStats() {
        wonGames = 0;
        wonSets = 0;
        lostSets = 0;
        points = 0;
        score = 0;
        scoreRatio = 0;
        rank = 0;
    }

    @Override
    public int getWonSets() {
        return wonSets;
    }

    @Override
    public void setWonSets(int wonSets) {
        this.wonSets = wonSets;
    }

    @Override
    public int getLostSets() {
        return lostSets;
    }

    @Override
    public void setLostSets(int lostSets) {
        this.lostSets = lostSets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(name);
        sb.append(", group '");
        sb.append(group.getName());
        sb.append("', rank ");
        sb.append(rank);
        sb.append(", points ");
        sb.append(points);
        sb.append(", won games ");
        sb.append(wonGames);
        sb.append(", won sets ");
        sb.append(wonSets);
        sb.append(", lost sets ");
        sb.append(lostSets);
        sb.append(", score ");
        sb.append(score);
        sb.append(", score ratio ");
        sb.append(scoreRatio);
        sb.append(">");

        return sb.toString();
    }

}
