package de.fhb.jeux.mockentity;

import de.fhb.jeux.model.IRoundSwitchRule;

public class MockRoundSwitchRuleEntity implements IRoundSwitchRule {

	private int srcGroupId;
	private int destGroupId;
	private int startWithRank;
	private int additionalPlayers;
	private int previousRoundId;

	@Override
	public int getSrcGroupId() {
		return srcGroupId;
	}

	@Override
	public int getDestGroupId() {
		return destGroupId;
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
		sb.append("[");
		sb.append("Source group ID = " + srcGroupId + ", ");
		sb.append("Destination group ID = " + destGroupId + ", ");
		sb.append("starting at rank " + startWithRank + ", ");
		sb.append("additional players: " + additionalPlayers);
		sb.append("]");
		return sb.toString();
	}
}
