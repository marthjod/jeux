package de.fhb.jeux.session;

import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dao.RankingDAO;
import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.persistence.*;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.model.IRanking;
import de.fhb.jeux.model.IRoundSwitchRule;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoundSwitchTakeoverTest {

    private RoundSwitchBean bean;
    private FinalRankingBean finalRankingBean;

    private IGroup destGroup1;
    private ShowdownGroup srcGroup1;
    private ShowdownGroup srcGroup2;
    List<ShowdownPlayer> srcGroup1Players;
    List<ShowdownPlayer> srcGroup2Players;
    private GroupDAO groupDAO;
    private GameDAO gameDAO;
    private RankingDAO rankingDAO;
    private RoundSwitchRuleDAO ruleDAO;
    private List<IRoundSwitchRule> rules;

    public RoundSwitchTakeoverTest() {
        rules = new ArrayList<>();
        srcGroup1Players = new ArrayList<>();
        srcGroup2Players = new ArrayList<>();
        groupDAO = mock(GroupDAO.class);
        gameDAO = mock(GameDAO.class);
        ruleDAO = mock(RoundSwitchRuleDAO.class);
        rankingDAO = mock(RankingDAO.class);
        finalRankingBean = new FinalRankingBean(rankingDAO);
        bean = new RoundSwitchBean(groupDAO, ruleDAO, new AdHocRankingBean(gameDAO),
                finalRankingBean);
    }

    private long maxGames(int numPlayers) {
        return CombinatoricsUtils.binomialCoefficient(numPlayers, 2);
    }

    @Before
    public void setUp() {
        srcGroup1 = new ShowdownGroup(1, "Source group 1", 1, 2, 3, true, true);
        srcGroup2 = new ShowdownGroup(2, "Source group 2", 1, 2, 3, true, true);
        destGroup1 = new ShowdownGroup(3, "Dest group 1", 2, 2, 3, false, false);

        List<IRanking> srcGroup1Rankings = new ArrayList<>();
        List<IRanking> srcGroup2Rankings = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            srcGroup1Players.add(new ShowdownPlayer(10 + i, "Player SG1." + i, 3, 10 * i, 0, 2, srcGroup1));
        }

        for (int i = 0; i < 3; i++) {
            srcGroup2Players.add(new ShowdownPlayer(20 + i, "Player SG2." + i, 3, 10 * i, 0, 2, srcGroup2));
        }

        srcGroup1.setPlayers(srcGroup1Players);
        srcGroup2.setPlayers(srcGroup2Players);

        for (IPlayer player : srcGroup1Players) {
            srcGroup1Rankings.add(new ShowdownRanking(player, player.getGroup()));
        }

        for (IPlayer player : srcGroup2Players) {
            srcGroup2Rankings.add(new ShowdownRanking(player, player.getGroup()));
        }

        when(gameDAO.getCountPlayedGamesInGroup(destGroup1)).
                thenReturn(maxGames(srcGroup1.getSize()));

        when(finalRankingBean.getRankings(srcGroup1)).thenReturn(srcGroup1Rankings);
        when(finalRankingBean.getRankings(srcGroup2)).thenReturn(srcGroup2Rankings);
    }

    @After
    public void tearDown() {
        rules.clear();
    }

    @Test
    public void doTakeover1SourceGroup1DestGroup() {
        rules.add(new ShowdownRoundSwitchRule(1, 2, srcGroup1, destGroup1));
        when(ruleDAO.getRulesForDestGroup(destGroup1)).thenReturn(rules);
        bean.doTakeover(destGroup1);
        for (IPlayer player : srcGroup1Players) {
            assertEquals(destGroup1, player.getGroup());
        }
    }

    @Test
    public void doTakeover2SourceGroups1DestGroup() {
        // first 2 of each group move on to next round
        rules.add(new ShowdownRoundSwitchRule(1, 2, srcGroup1, destGroup1));
        rules.add(new ShowdownRoundSwitchRule(1, 2, srcGroup2, destGroup1));
        when(ruleDAO.getRulesForDestGroup(destGroup1)).thenReturn(rules);
        bean.doTakeover(destGroup1);
        for (IPlayer player : srcGroup1Players) {
            assertEquals(destGroup1, player.getGroup());
        }
        for (IPlayer player : srcGroup2Players) {
            assertEquals(destGroup1, player.getGroup());
        }
    }

}
