package de.fhb.jeux.config;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BonusPointsDistributorTest {

    private List<BonusPointsRule> ruleList;
    private BonusPointsDistribution dist;

    @Before
    public void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(new BonusPointsRule(2, 3, 3, 1));
        ruleList.add(new BonusPointsRule(2, 2, 4, 0));
    }

}
