package de.fhb.jeux.dto;

import de.fhb.jeux.model.IRoundSwitchRule;

public class RoundSwitchRuleDTO {
	
	public RoundSwitchRuleDTO() {
		
	}
	
	public RoundSwitchRuleDTO(IRoundSwitchRule roundSwitchRuleEntity) {
		this.srcGroupId = roundSwitchRuleEntity.getSrcGroupId();
		this.destGroupId = roundSwitchRuleEntity.getDestGroupId();
		this.previousRoundId = roundSwitchRuleEntity.getPreviousRoundId();
		this.startWithRank = roundSwitchRuleEntity.getStartWithRank();
		this.additionalPlayers = roundSwitchRuleEntity.getAdditionalPlayers();
	}
	
	private int srcGroupId;
	private int destGroupId;
	private int previousRoundId;
	private int startWithRank;
	private int additionalPlayers;
	
	
	public int getSrcGroupId() {
		return srcGroupId;
	}

	public int getDestGroupId() {
		return destGroupId;
	}

	public int getPreviousRoundId() {
		return previousRoundId;
	}

	public int getStartWithRank() {
		return startWithRank;
	}

	public int getAdditionalPlayers() {
		return additionalPlayers;
	}
	
	

}
