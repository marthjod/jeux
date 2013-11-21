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

import de.fhb.jeux.model.IRoundSwitchRule;

@Entity
@Table(name = "RoundSwitchRule")
@NamedQueries({	@NamedQuery(name = "RoundSwitchRule.findAll", query  = "SELECT r FROM ShowdownRoundSwitchRule r"),
				@NamedQuery(name = "RoundSwitchRule.findById", query = "SELECT r FROM ShowdownRoundSwitchRule r WHERE r.id = :id")})
public class ShowdownRoundSwitchRule implements IRoundSwitchRule, Serializable{
	
	private static final long serialVersionUID = -1174601853867495185L;
	protected static Logger logger = Logger.getLogger(ShowdownRoundSwitchRule.class);

	public ShowdownRoundSwitchRule() {
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
	
	

}
