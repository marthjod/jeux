package de.fhb.jeux.session;

import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.persistence.*;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRoundSwitchRule;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoundSwitchBeanTest {

    private RoundSwitchBean bean;
    private IGroup destGroup;
    private IGroup srcGroup;
    private RoundSwitchRuleDAO ruleDAO;
    private List<IRoundSwitchRule> rules;

    public RoundSwitchBeanTest() {
        bean = new RoundSwitchBean();
        rules = new ArrayList<>();
    }

    @Before
    public void setUp() {
        destGroup = mock(ShowdownGroup.class);
        srcGroup = mock(ShowdownGroup.class);
        ruleDAO = mock(RoundSwitchRuleDAO.class);
    }

    @Test
    public void checkDestGroupFailsNull() {
        assertFalse(bean.checkDestGroup(null));
    }

    @Test
    public void checkDestGroupFailsCompleted() {
        when(destGroup.isCompleted()).thenReturn(Boolean.TRUE);
        assertFalse(bean.checkDestGroup(destGroup));
    }

    @Test
    public void checkDestGroupFailsHasGames() {
        when(destGroup.hasGames()).thenReturn(Boolean.TRUE);
        assertFalse(bean.checkDestGroup(destGroup));
    }

    @Test
    public void checkDestGroupOk() {
        when(destGroup.isCompleted()).thenReturn(Boolean.FALSE);
        when(destGroup.hasGames()).thenReturn(Boolean.FALSE);
        assertTrue(bean.checkDestGroup(destGroup));
    }

    @Test
    public void checkSrcGroupsFailsRulesEmpty() {
        assertFalse(bean.checkSrcGroups(destGroup,
                new ArrayList<IRoundSwitchRule>()));
    }

    @Test
    public void checkSrcGroupsFailsIncomplete() {
        when(srcGroup.isCompleted()).thenReturn(Boolean.FALSE);
        assertFalse(bean.checkSrcGroups(destGroup, rules));
    }

    @Test
    public void checkSrcGroupsOk() {
        when(srcGroup.isCompleted()).thenReturn(Boolean.TRUE);
        rules.add(new ShowdownRoundSwitchRule(1, 1,
                0, srcGroup, destGroup));
        assertTrue(bean.checkSrcGroups(destGroup, rules));
    }
}
