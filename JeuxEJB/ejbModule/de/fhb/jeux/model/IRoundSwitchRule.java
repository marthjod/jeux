package de.fhb.jeux.model;

public interface IRoundSwitchRule {
	public IGroup getSrcGroup();

	public void setSrcGroup(IGroup srcGroup);

	public void setDestGroup(IGroup destGroup);

	public IGroup getDestGroup();

	public int getStartWithRank();

	public int getAdditionalPlayers();

	public int getPreviousRoundId();

}
