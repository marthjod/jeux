package de.fhb.jeux.model;

public interface IRoundSwitchRule {
	public int getSrcGroupId();

	public int getDestGroupId();

	public int getStartWithRank();

	public int getAdditionalPlayers();

	public int getPreviousRoundId();

}
