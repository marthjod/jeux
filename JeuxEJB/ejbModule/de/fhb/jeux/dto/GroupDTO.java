package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;

// "flat" representation of Entity object better suitable 
// for generating data interchange format (JSON) from
public class GroupDTO {

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
		sb.append("'" + name + "' (" + id + ")");
		sb.append(", min sets: " + minSets);
		sb.append(", max sets: " + maxSets);
		sb.append(", round ID: " + roundId);
		sb.append(", active: " + active);
		sb.append(", completed: " + completed);
		sb.append(">");
		return sb.toString();
	}

}
