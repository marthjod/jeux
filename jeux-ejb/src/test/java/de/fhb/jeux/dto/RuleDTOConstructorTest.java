package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownGroup;
import de.fhb.jeux.persistence.ShowdownRoundSwitchRule;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RuleDTOConstructorTest {

    private IGroup srcGroup;
    private IGroup destGroup;
    private ShowdownRoundSwitchRule rule;
    private RuleDTO ruleDTO;

    public RuleDTOConstructorTest() {
        srcGroup = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
        destGroup = new ShowdownGroup(12, "Group B", 2, 2, 3, false, false);
        rule = new ShowdownRoundSwitchRule(1, 1, 0, srcGroup, destGroup);
    }

    @Before
    public void setUp() {
        ruleDTO = new RuleDTO(rule);
    }

    @Test
    public void constructorPreviosRoundId() {
        assertEquals(ruleDTO.getPreviousRoundId(), rule.getPreviousRoundId());
    }

    @Test
    public void constructorStartWithRank() {
        assertEquals(ruleDTO.getStartWithRank(), rule.getStartWithRank());
    }

    @Test
    public void constructorAdditionalPlayers() {
        assertEquals(ruleDTO.getAdditionalPlayers(), rule.getAdditionalPlayers());
    }

    @Test
    public void constructorSrcGroupId() {
        assertEquals(ruleDTO.getSrcGroupId(), rule.getSrcGroup().getId());
    }

    @Test
    public void constructorSrcGroupName() {
        assertEquals(ruleDTO.getSrcGroupName(), rule.getSrcGroup().getName());
    }

    @Test
    public void constructorDestGroupId() {
        assertEquals(ruleDTO.getDestGroupId(), rule.getDestGroup().getId());
    }

    @Test
    public void constructorDestGroupName() {
        assertEquals(ruleDTO.getDestGroupName(), rule.getDestGroup().getName());
    }

}
