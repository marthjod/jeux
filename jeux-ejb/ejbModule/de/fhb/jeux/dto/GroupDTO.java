package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import java.util.ArrayList;
import java.util.List;

// "flat" representation of Entity object better suitable
// for generating data interchange format (JSON) from
public class GroupDTO implements Comparable<GroupDTO> {

    private transient int id;
    private String name;
    private int minSets;
    private int maxSets;
    private boolean active;
    private boolean completed;
    private int roundId;
    private boolean hasGames;
    private int size;
    private List<String> players = new ArrayList<>();

    // argument-less constructor for converter libs (Jackson...)
    // client-provided data -> DTO
    public GroupDTO() {
    }

    public GroupDTO(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    // parametrized constructor for conversion from IGroup implementations
    // (i.e. Persistent Entities)
    // Entity -> DTO
    public GroupDTO(IGroup groupEntity) {
        this.id = groupEntity.getId();
        this.name = groupEntity.getName();
        this.active = groupEntity.isActive();
        this.completed = groupEntity.isCompleted();
        this.minSets = groupEntity.getMinSets();
        this.maxSets = groupEntity.getMaxSets();
        this.roundId = groupEntity.getRoundId();
        this.hasGames = groupEntity.hasGames();
        this.size = groupEntity.getSize();
        for (IPlayer player : groupEntity.getPlayers()) {
            this.players.add(player.getName());
        }
    }

    public GroupDTO(String name, int minSets, int maxSets, int roundId,
            boolean active, boolean completed) {
        this.name = name;
        this.minSets = minSets;
        this.maxSets = maxSets;
        this.roundId = roundId;
        this.active = active;
        this.completed = completed;
    }

    public GroupDTO(int id, String name, int minSets, int maxSets, int roundId,
            boolean active, boolean completed) {
        this(name, minSets, maxSets, roundId, active, completed);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinSets() {
        return minSets;
    }

    public int getMaxSets() {
        return maxSets;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getRoundId() {
        return roundId;
    }

    public boolean hasGames() {
        return this.hasGames;
    }

    public int getSize() {
        return this.size;
    }

    public List<String> getPlayers() {
        return this.players;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append("'").append(name).append("' (").append(id).append(")");
        sb.append(", min sets: ").append(minSets);
        sb.append(", max sets: ").append(maxSets);
        sb.append(", round ID: ").append(roundId);
        sb.append(", active: ").append(active);
        sb.append(", completed: ").append(completed);
        sb.append(", size: ").append(size);
        sb.append(", has games: ").append(hasGames);
        sb.append(">");
        return sb.toString();
    }

    @Override
    public int compareTo(GroupDTO dto) {
        return this.roundId - dto.getRoundId();
    }

}
