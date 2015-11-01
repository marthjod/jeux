package de.fhb.jeux.bulkimport;

public class ImportRule {

    private String srcGroupName;
    private String destGroupName;
    private int startWithRank;
    private int additionalPlayers;

    public ImportRule() {
    }

    public String getSrcGroupName() {
        return srcGroupName;
    }

    public String getDestGroupName() {
        return destGroupName;
    }

    public int getStartWithRank() {
        return startWithRank;
    }

    public int getAdditionalPlayers() {
        return additionalPlayers;
    }

}
