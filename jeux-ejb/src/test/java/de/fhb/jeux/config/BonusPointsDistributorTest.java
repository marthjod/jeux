package de.fhb.jeux.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BonusPointsDistributorTest {

    private BonusPointsDistribution distribution;
    private BonusPointsDistributor distributor;
    private HashMap<String, Integer> expectedBonusPoints;

    @Before
    public void setUp() {
        List<BonusPointsRule> ruleList = new ArrayList<>();
        ruleList.add(new BonusPointsRule(2, 3, 3, 1));
        ruleList.add(new BonusPointsRule(2, 2, 4, 0));
        distribution = new BonusPointsDistribution(ruleList);
        distributor = new BonusPointsDistributor(distribution);
    }

    @Test
    public void distributorWinnerIn2Sets() {
        expectedBonusPoints = new HashMap<>();
        expectedBonusPoints.put("winner", 4);
        expectedBonusPoints.put("loser", 0);
        assertEquals(expectedBonusPoints, distributor.getBonusPoints(2, 2));
    }

    @Test
    public void distributorWinnerIn3Sets() {
        expectedBonusPoints = new HashMap<>();
        expectedBonusPoints.put("winner", 3);
        expectedBonusPoints.put("loser", 1);
        assertEquals(expectedBonusPoints, distributor.getBonusPoints(3, 2));
    }

    @Test
    public void distributorUnknownInput() {
        expectedBonusPoints = new HashMap<>();
        expectedBonusPoints.put("winner", 0);
        expectedBonusPoints.put("loser", 0);
        assertEquals(expectedBonusPoints, distributor.getBonusPoints(10, 10));
    }
}
