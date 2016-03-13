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
import org.junit.Ignore;
import static org.mockito.Mockito.*;

// https://github.com/marthjod/jeux/issues/57
// test if after having applied one rule, all following rules are still
// applied correctly
//
// reproduce bug scenario:
// - 1 source group with 2 or more players
// - 2 dest groups
// - 2 rules: move rank 1 to dest group 1, move rank 2 to dest group 2
// - when rules are being applied during takeover, an ad-hoc ranking
//   is used, i.e. ranks are determined anew each time
// - if original rank 1 has already been moved, original rank 2 *becomes*
//   the new rank 1 after ad-hoc ranking (this is the cause for the bug)
// - this leads to 2 different errors:
//   a. if enough ranks are available in the source group, wrong players
//      will be moved (rule 2 above would effectively move the original
//      rank 3!)
//   b. IndexOutOfBoundsException if not enough ranks are available in the
//      source group for every additional takeover: if source group has
//      only 2 players, then rule 2 above would look for rank 2, but only find
//      (the newly-ranked) rank 1 left.
public class RoundSwitchMultiTakeoverTest {

    private RoundSwitchBean bean;
    private FinalRankingBean finalRankingBean;

    private IGroup destGroup1;
    private IGroup destGroup2;
    private ShowdownGroup srcGroup;
    List<ShowdownPlayer> srcGroupPlayers;
    List<IRoundSwitchRule> destGroup1Rules;
    List<IRoundSwitchRule> destGroup2Rules;
    List<IRanking> srcGroupRankings;
    private GroupDAO groupDAO;
    private GameDAO gameDAO;
    private RankingDAO rankingDAO;
    private RoundSwitchRuleDAO ruleDAO;

    public RoundSwitchMultiTakeoverTest() {
        srcGroupPlayers = new ArrayList<>();
        srcGroupRankings = new ArrayList<>();
        destGroup1Rules = new ArrayList<>();
        destGroup2Rules = new ArrayList<>();

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
        srcGroup = new ShowdownGroup(1, "Source group 1", 1, 2, 3, true, true);
        destGroup1 = new ShowdownGroup(3, "Dest group 1", 2, 2, 3, false, false);
        destGroup2 = new ShowdownGroup(4, "Dest group 2", 2, 2, 3, false, false);

        destGroup1Rules.add(new ShowdownRoundSwitchRule(1, 0, srcGroup, destGroup1));
        destGroup2Rules.add(new ShowdownRoundSwitchRule(2, 0, srcGroup, destGroup2));

        when(ruleDAO.getRulesForDestGroup(destGroup1)).thenReturn(destGroup1Rules);
        when(ruleDAO.getRulesForDestGroup(destGroup2)).thenReturn(destGroup2Rules);

    }

    @After
    public void tearDown() {
        srcGroupRankings.clear();
        destGroup1Rules.clear();
        destGroup2Rules.clear();
    }

    @Ignore
    private void setSrcGroupPlayers(int numberOfPlayers) {
        srcGroupPlayers.clear();
        for (int i = 0; i < numberOfPlayers; i++) {
            srcGroupPlayers.add(new ShowdownPlayer(10 + i, "Player SG1." + i, 3, 10 * i, 0, 2, srcGroup));
        }
        srcGroup.setPlayers(srcGroupPlayers);
        for (IPlayer player : srcGroupPlayers) {
            srcGroupRankings.add(new ShowdownRanking(player, player.getGroup()));
        }

        when(finalRankingBean.getRankings(srcGroup)).thenReturn(srcGroupRankings);
    }

    @Test
    public void doTakeover1SourceGroup2DestGroupsCorrectRanksMoved() {
        setSrcGroupPlayers(3);

        assertEquals(0, destGroup1.getSize());
        assertEquals(0, destGroup2.getSize());
        assertEquals(3, srcGroup.getSize());

        bean.doTakeover(destGroup1);
        assertEquals(1, destGroup1.getSize());
        assertEquals(2, srcGroup.getSize());

        assertEquals(10, destGroup1.getPlayers().get(0).getId());
        assertEquals("Player SG1.0", destGroup1.getPlayers().get(0).getName());

        bean.doTakeover(destGroup2);
        assertEquals(1, destGroup1.getSize());
        assertEquals(1, destGroup2.getSize());
        assertEquals(1, srcGroup.getSize());

        assertEquals(11, destGroup2.getPlayers().get(0).getId());
        assertEquals("Player SG1.1", destGroup2.getPlayers().get(0).getName());
    }

    @Test
    public void doTakeover1SourceGroup2DestGroupsIndexOk() {
        setSrcGroupPlayers(2);

        assertEquals(0, destGroup1.getSize());
        assertEquals(0, destGroup2.getSize());
        assertEquals(2, srcGroup.getSize());

        bean.doTakeover(destGroup1);
        assertEquals(1, destGroup1.getSize());
        assertEquals(1, srcGroup.getSize());

        assertEquals(10, destGroup1.getPlayers().get(0).getId());
        assertEquals("Player SG1.0", destGroup1.getPlayers().get(0).getName());

        bean.doTakeover(destGroup2);
        assertEquals(1, destGroup1.getSize());
        assertEquals(1, destGroup2.getSize());
        assertEquals(0, srcGroup.getSize());

        assertEquals(11, destGroup2.getPlayers().get(0).getId());
        assertEquals("Player SG1.1", destGroup2.getPlayers().get(0).getName());
    }
}
