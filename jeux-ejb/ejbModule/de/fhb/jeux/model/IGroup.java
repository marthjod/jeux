package de.fhb.jeux.model;

import java.util.List;

import de.fhb.jeux.persistence.ShowdownPlayer;

public interface IGroup {

    public int getId();

    public String getName();

    public int getRoundId();

    public int getMinSets();

    public int getMaxSets();

    public int getSize();

    public boolean hasGames();

    public List<ShowdownPlayer> getPlayers();

    public boolean isActive();

    public void setActive(boolean active);

    public boolean isCompleted();

    public void setCompleted(boolean completed);

    public boolean equals(IGroup group);
}
