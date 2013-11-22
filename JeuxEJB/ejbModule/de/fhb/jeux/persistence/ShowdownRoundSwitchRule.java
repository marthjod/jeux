package de.fhb.jeux.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jboss.logging.Logger;

import de.fhb.jeux.dto.RoundSwitchRuleDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRoundSwitchRule;

@Entity
@Table(name = "RoundSwitchRule")
@NamedQueries({
		@NamedQuery(name = "RoundSwitchRule.findAll", query = "SELECT r FROM ShowdownRoundSwitchRule r"),
		@NamedQuery(name = "RoundSwitchRule.findById", query = "SELECT r FROM ShowdownRoundSwitchRule r WHERE r.id = :id") })
public class ShowdownRoundSwitchRule implements IRoundSwitchRule, Serializable {

	private static final long serialVersionUID = -1174601853867495185L;
	protected static Logger logger = Logger
			.getLogger(ShowdownRoundSwitchRule.class);

	public ShowdownRoundSwitchRule() {
	}

	public ShowdownRoundSwitchRule(RoundSwitchRuleDTO ruleDTO) {
		this.previousRoundId = ruleDTO.getPreviousRoundId();
		this.startWithRank = ruleDTO.getStartWithRank();
		this.additionalPlayers = ruleDTO.getAdditionalPlayers();
		// this.srcGroup =
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private int previousRoundId;

	@Column
	private int startWithRank;

	@Column
	private int additionalPlayers;

	@OneToOne
	@JoinColumn(name = "srcGroupId")
	private ShowdownGroup srcGroup;

	@OneToOne
	@JoinColumn(name = "destGroupId")
	private ShowdownGroup destGroup;

	@Override
	public int getSrcGroupId() {
		return srcGroup.getId();
	}

	@Override
	public void setSrcGroup(IGroup srcGroup) {
		this.srcGroup = (ShowdownGroup) srcGroup;
	}

	@Override
	public void setDestGroup(IGroup destGroup) {
		this.destGroup = (ShowdownGroup) destGroup;
	}

	@Override
	public int getDestGroupId() {
		return destGroup.getId();
	}

	@Override
	public int getStartWithRank() {
		return startWithRank;
	}

	@Override
	public int getAdditionalPlayers() {
		return additionalPlayers;
	}

	@Override
	public int getPreviousRoundId() {
		return previousRoundId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append("Previous round: ");
		sb.append(this.previousRoundId);
		sb.append(", starting with rank: ");
		sb.append(this.startWithRank);
		sb.append(", additional players: ");
		sb.append(this.additionalPlayers);
		sb.append(", source group: ");
		sb.append(this.srcGroup.getName());
		sb.append(", destination group: ");
		sb.append(this.destGroup.getName());
		sb.append(">");

		return sb.toString();
	}

}
