package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;

// "flat" representation of Entity object better suitable
// for generating data interchange format (JSON) from
public class GroupDTO implements Comparable {

    private int id;
    private String name;
    private int minSets;
    private int maxSets;
    private boolean active;
    private boolean completed;
    private int roundId;

    // argument-less constructor for converter libs (Jackson...)
    // client-provided data -> DTO
    public GroupDTO() {
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
    }

    public GroupDTO(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
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
        sb.append(">");
        return sb.toString();
    }

    @Override
    public int compareTo(Object group) {
        return this.name.compareTo(((GroupDTO) group).getName());
    }

}
