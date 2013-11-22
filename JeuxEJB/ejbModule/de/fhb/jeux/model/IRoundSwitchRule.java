package de.fhb.jeux.model;

public interface IRoundSwitchRule {
	public int getSrcGroupId();

	public void setSrcGroup(IGroup srcGroup);

	public void setDestGroup(IGroup destGroup);

	public int getDestGroupId();

	public int getStartWithRank();

	public int getAdditionalPlayers();

	public int getPreviousRoundId();

}
