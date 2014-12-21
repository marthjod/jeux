package de.fhb.jeux.dto;

import de.fhb.jeux.model.IRoundSwitchRule;

public class RoundSwitchRuleDTO {

	public RoundSwitchRuleDTO() {

	}

	public RoundSwitchRuleDTO(IRoundSwitchRule roundSwitchRuleEntity) {
		id = roundSwitchRuleEntity.getId();
		srcGroupId = roundSwitchRuleEntity.getSrcGroup().getId();
		srcGroupName = roundSwitchRuleEntity.getSrcGroup().getName();
		destGroupId = roundSwitchRuleEntity.getDestGroup().getId();
		destGroupName = roundSwitchRuleEntity.getDestGroup().getName();
		previousRoundId = roundSwitchRuleEntity.getPreviousRoundId();
		startWithRank = roundSwitchRuleEntity.getStartWithRank();
		additionalPlayers = roundSwitchRuleEntity.getAdditionalPlayers();
	}

	private int id;
	private int srcGroupId;
	private int destGroupId;
	private String srcGroupName;
	private String destGroupName;
	private int previousRoundId;
	private int startWithRank;
	private int additionalPlayers;

	public int getId() {
		return id;
	}

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

	public String getSrcGroupName() {
		return srcGroupName;
	}

	public String getDestGroupName() {
		return destGroupName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<[");
		sb.append(id);
		sb.append("] ");
		sb.append(srcGroupName);
		sb.append(" #");
		sb.append(startWithRank);
		sb.append("-#");
		sb.append(startWithRank + additionalPlayers);
		sb.append(" (");
		sb.append(additionalPlayers + 1);
		sb.append(") --> ");
		sb.append(destGroupName);
		sb.append(">");

		return sb.toString();
	}

}
