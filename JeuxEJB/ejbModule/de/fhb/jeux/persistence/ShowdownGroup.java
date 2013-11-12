package de.fhb.jeux.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;

@Entity
// MySQL does not allow "Group" for table name
// http://dev.mysql.com/doc/refman/5.1/en/reserved-words.html
@Table(name = "Group_")
@NamedQueries({
		@NamedQuery(name = "Group.findAll", query = "SELECT g FROM ShowdownGroup g"),
		@NamedQuery(name = "Group.findById", query = "SELECT g FROM ShowdownGroup g WHERE g.id = :id") })
public class ShowdownGroup implements IGroup, Serializable {

	private static final long serialVersionUID = 4301340014397931722L;

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

	@OneToMany(mappedBy = "group")
	private List<ShowdownPlayer> players;

	// needed by @Entity
	public ShowdownGroup() {
	}

	// constructor for converting DTO to Entity
	public ShowdownGroup(GroupDTO groupDTO) {
		this.id = groupDTO.getId();
		this.name = groupDTO.getName();
		this.active = groupDTO.isActive();
		this.completed = groupDTO.isCompleted();
		this.minSets = groupDTO.getMinSets();
		this.maxSets = groupDTO.getMaxSets();
		this.roundId = groupDTO.getRoundId();
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

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("'");
		sb.append(this.name);
		sb.append("' (ID ");
		sb.append(this.id);
		sb.append("), round ");
		sb.append(this.roundId);
		sb.append(", active = ");
		sb.append(this.active);
		sb.append(", completed = ");
		sb.append(this.completed);
		sb.append(", ");
		sb.append(this.minSets);
		sb.append("-");
		sb.append(this.maxSets);
		sb.append(" sets");

		return sb.toString();
	}

	public List<ShowdownPlayer> getPlayers() {
		return players;
	}

	@Override
	public boolean equals(IGroup group) {
		// TODO
		return false;
	}

}
