package de.fhb.jeux.bulkimport;

import java.util.List;

public class ImportGroup {

    private String name;
    private int minSets;
    private int maxSets;
    private int roundId;
    private boolean active;
    private boolean completed;
    private List<String> players;

    public ImportGroup() {
    }

    public String getName() {
        return name;
    }

    public int getMinSets() {
        return minSets;
    }

    public int getMaxSets() {
        return maxSets;
    }

    public int getRoundId() {
        return roundId;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCompleted() {
        return completed;
    }

    public List<String> getPlayers() {
        return players;
    }
}
