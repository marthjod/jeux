package de.fhb.jeux.persistence;

import de.fhb.jeux.dto.RuleDTO;
import de.fhb.jeux.model.IGroup;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ShowdownRoundSwitchRuleTest {

    private IGroup srcGroup;
    private IGroup destGroup;
    private ShowdownRoundSwitchRule rule;
    private RuleDTO ruleDTO;

    public ShowdownRoundSwitchRuleTest() {
        srcGroup = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
        destGroup = new ShowdownGroup(13, "Group B", 2, 2, 3, false, false);
        ruleDTO = new RuleDTO(srcGroup.getName(),
                destGroup.getName(), 1, 0);
    }

    @Before
    public void setUp() {
        rule = new ShowdownRoundSwitchRule(ruleDTO, srcGroup, destGroup);
    }

    @Test
    public void constructorStartWithRank() {
        assertEquals(rule.getStartWithRank(), ruleDTO.getStartWithRank());
    }

    @Test
    public void constructorAdditionalPlayers() {
        assertEquals(rule.getAdditionalPlayers(), ruleDTO.getAdditionalPlayers());
    }

    @Ignore("DTO does not set group ID in this case")
    @Test
    public void constructorSrcGroupId() {
        assertEquals(rule.getSrcGroup().getId(), ruleDTO.getSrcGroupId());
    }

    @Test
    public void constructorSrcGroupName() {
        assertEquals(rule.getSrcGroup().getName(), ruleDTO.getSrcGroupName());
    }

    @Ignore("DTO does not set group ID in this case")
    @Test
    public void constructorDestGroupId() {
        assertEquals(rule.getDestGroup().getId(), ruleDTO.getDestGroupId());
    }

    @Test
    public void constructorDestGroupName() {
        assertEquals(rule.getDestGroup().getName(), ruleDTO.getDestGroupName());
    }
}
