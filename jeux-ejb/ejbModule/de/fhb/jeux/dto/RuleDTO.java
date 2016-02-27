package de.fhb.jeux.dto;

import de.fhb.jeux.model.IRoundSwitchRule;

public class RuleDTO {

    private transient int id;
    private transient int srcGroupId;
    private transient int destGroupId;
    private String srcGroupName;
    private String destGroupName;
    private int previousRoundId;
    private int startWithRank;
    private int additionalPlayers;

    public RuleDTO() {
    }

    public RuleDTO(IRoundSwitchRule roundSwitchRuleEntity) {
        id = roundSwitchRuleEntity.getId();
        srcGroupId = roundSwitchRuleEntity.getSrcGroup().getId();
        srcGroupName = roundSwitchRuleEntity.getSrcGroup().getName();
        destGroupId = roundSwitchRuleEntity.getDestGroup().getId();
        destGroupName = roundSwitchRuleEntity.getDestGroup().getName();
        previousRoundId = roundSwitchRuleEntity.getPreviousRoundId();
        startWithRank = roundSwitchRuleEntity.getStartWithRank();
        additionalPlayers = roundSwitchRuleEntity.getAdditionalPlayers();
    }

    public RuleDTO(String srcGroupName, String destGroupName,
            int startWithRank, int additionalPlayers) {
        this.srcGroupName = srcGroupName;
        this.destGroupName = destGroupName;
        this.startWithRank = startWithRank;
        this.additionalPlayers = additionalPlayers;
    }

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
