package de.fhb.jeux.config;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BonusPointsRuleTest {

    private BonusPointsRule rule;

    @Before
    public void setUp() {
        rule = new BonusPointsRule(2, 3, 3, 1);
    }

    @Test
    public void constructor() {
        BonusPointsRule rule2 = new BonusPointsRule();
        assertTrue(rule2 instanceof BonusPointsRule);
    }

    @Test
    public void getSetsWonByWinner() {
        assertEquals(rule.getSetsWonByWinner(), 2);
    }

    @Test
    public void getTotalSets() {
        assertEquals(rule.getTotalSets(), 3);
    }

    @Test
    public void getBonusPointsWinner() {
        assertEquals(rule.getBonusPointsWinner(), 3);
    }

    @Test
    public void getBonusPointsLoser() {
        assertEquals(rule.getBonusPointsLoser(), 1);
    }
}
