package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownGroup;
import de.fhb.jeux.persistence.ShowdownPlayer;
import de.fhb.jeux.persistence.ShowdownRanking;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RankingDTOConstructorTest {

    private IGroup group;
    private IPlayer player;
    private ShowdownRanking ranking;
    private RankingDTO rankingDTO;

    public RankingDTOConstructorTest() {
        group = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
        player = new ShowdownPlayer(23, "Player A", 15, 7, 2, 1,
                (ShowdownGroup) group);
        ranking = new ShowdownRanking(player, group);
    }

    @Before
    public void setUp() {
        rankingDTO = new RankingDTO(ranking);
    }

    @Test
    public void constructorPlayerId() {
        assertEquals(rankingDTO.getPlayerId(), ranking.getPlayer().getId());
    }

    @Test
    public void constructorPlayerName() {
        assertEquals(rankingDTO.getPlayerName(), ranking.getPlayer().getName());
    }

    @Test
    public void constructorGroupName() {
        assertEquals(rankingDTO.getGroupName(), ranking.getGroup().getName());
    }

    @Test
    public void constructorBonuspoints() {
        assertEquals(rankingDTO.getPoints(), ranking.getPoints());
    }

    @Test
    public void constructorScoreRatio() {
        assertEquals(rankingDTO.getScoreRatio(), ranking.getScoreRatio());
    }

    @Test
    public void constructorRank() {
        assertEquals(rankingDTO.getRank(), ranking.getRank());
    }

    @Test
    public void constructorWonGames() {
        assertEquals(rankingDTO.getWonGames(), ranking.getWonGames());
    }

}
