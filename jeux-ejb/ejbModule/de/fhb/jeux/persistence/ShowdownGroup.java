package de.fhb.jeux.persistence;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.jboss.logging.Logger;

@Entity
// MySQL does not allow "Group" for table name
// http://dev.mysql.com/doc/refman/5.1/en/reserved-words.html
@Table(name = "Group_")
@NamedQueries({
    @NamedQuery(name = "Group.findAll", query = "SELECT g FROM ShowdownGroup g"),
    @NamedQuery(name = "Group.findById", query = "SELECT g FROM ShowdownGroup g WHERE g.id = :id"),
    @NamedQuery(name = "Group.findByName", query = "SELECT g FROM ShowdownGroup g WHERE g.name = :name"),
    @NamedQuery(name = "Group.findCompleteInRound", query = "SELECT g FROM ShowdownGroup g WHERE g.roundId = :roundId AND g.completed = true"),
    @NamedQuery(name = "Group.findIncompleteInRound", query = "SELECT g FROM ShowdownGroup g WHERE g.roundId = :roundId AND g.completed = false"),
    @NamedQuery(name = "Group.findCurrentRoundId", query = "SELECT MAX(g.roundId) FROM ShowdownGroup g WHERE g.completed = true OR g.active = true")})
public class ShowdownGroup implements IGroup, Serializable {

    private static final long serialVersionUID = 4301340014397931722L;

    protected static Logger logger = Logger.getLogger(ShowdownGroup.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private int roundId;

    @Column
    private int minSets;

    @Column
    private int maxSets;

    @Column
    private boolean active;

    @Column
    private boolean completed;

    @Transient
    private int size;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    private List<ShowdownPlayer> players;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    private List<ShowdownGame> games;

    // needed by @Entity
    public ShowdownGroup() {
    }

    public ShowdownGroup(int id, String name, int roundId, int minSets,
            int maxSets, boolean active, boolean completed) {
        this.id = id;
        this.name = name;
        this.roundId = roundId;
        this.minSets = minSets;
        this.maxSets = maxSets;
        this.active = active;
        this.completed = completed;
        this.games = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    // constructor for converting DTO to Entity
    public ShowdownGroup(GroupDTO groupDTO) {
        this(groupDTO.getId(), groupDTO.getName(), groupDTO.getRoundId(),
                groupDTO.getMinSets(), groupDTO.getMaxSets(),
                groupDTO.isActive(), groupDTO.isCompleted());
    }

    @Override
    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    @Override
    public int getMinSets() {
        return minSets;
    }

    public void setMinSets(int minSets) {
        this.minSets = minSets;
    }

    @Override
    public int getMaxSets() {
        return maxSets;
    }

    public void setMaxSets(int maxSets) {
        this.maxSets = maxSets;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive() {
        this.active = true;
    }

    @Override
    public void setInactive() {
        this.active = false;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void setCompleted() {
        this.completed = true;
    }

    @Override
    public void setIncomplete() {
        this.completed = false;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getSize() {
        return this.players.size();
    }

    @Override
    public List<ShowdownPlayer> getPlayers() {
        return this.players;
    }

    @Override
    public void setPlayers(List<ShowdownPlayer> players) {
        this.players = players;
    }

    @Override
    public boolean hasGames() {
        return this.games.size() > 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append("'");
        sb.append(name);
        sb.append("' (ID ");
        sb.append(id);
        sb.append("), ");
        sb.append(getSize());
        if (getSize() == 1) {
            sb.append(" player");
        } else {
            sb.append(" players");
        }
        sb.append(", round ");
        sb.append(roundId);
        sb.append(", active = ");
        sb.append(active);
        sb.append(", completed = ");
        sb.append(completed);
        sb.append(", ");
        sb.append(minSets);
        sb.append("-");
        sb.append(maxSets);
        sb.append(" sets");
        sb.append(">");

        return sb.toString();
    }

    @Override
    public boolean equals(IGroup group) {
        // assuming unique IDs as sufficient for identity
        return this.id == group.getId();
    }
}
