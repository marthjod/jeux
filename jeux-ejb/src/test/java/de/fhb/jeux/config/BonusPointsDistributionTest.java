package de.fhb.jeux.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class BonusPointsDistributionTest {

    private List<BonusPointsRule> ruleList;
    private BonusPointsDistribution dist;
    private String mockConfigContent;
    private File tempFile;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[ { \"setsWonByWinner\":2,  \"totalSets\":2, ");
        sb.append("\"bonusPointsWinner\":4,  \"bonusPointsLoser\":0 }, ");
        sb.append("{  \"setsWonByWinner\":2,  \"totalSets\":3, ");
        sb.append("\"bonusPointsWinner\":3,  \"bonusPointsLoser\":1 } ]");
        mockConfigContent = sb.toString();

        ruleList = BonusPointsDistribution.marshalJson(mockConfigContent);
        tempFile = tempFolder.newFile("mock - config.json");
        FileUtils.writeStringToFile(tempFile, mockConfigContent);
    }

    @Test
    public void constructor() throws IOException {
        dist = new BonusPointsDistribution(tempFile.getAbsolutePath());
        assertTrue(dist instanceof BonusPointsDistribution);
    }

    @Test
    public void constructorReadsRuleLists() throws IOException {
        dist = new BonusPointsDistribution(tempFile.getAbsolutePath());
        assertEquals(2, dist.getRules().size());
        assertEquals(ruleList, dist.getRules());
    }
}
