package de.fhb.jeux.dto;

import de.fhb.jeux.model.IRoundSwitchRule;

public class RoundSwitchRuleDTO {

	public RoundSwitchRuleDTO() {

	}

	public RoundSwitchRuleDTO(IRoundSwitchRule roundSwitchRuleEntity) {
		this.srcGroupId = roundSwitchRuleEntity.getSrcGroup().getId();
		this.destGroupId = roundSwitchRuleEntity.getDestGroup().getId();
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
		sb.append(this.srcGroupId);
		sb.append(", destination group: ");
		sb.append(this.destGroupId);
		sb.append(">");

		return sb.toString();
	}

}
