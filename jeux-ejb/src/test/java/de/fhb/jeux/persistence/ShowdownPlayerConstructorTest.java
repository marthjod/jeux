package de.fhb.jeux.persistence;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class ShowdownPlayerConstructorTest extends TestCase {

    private IGroup group;
    private PlayerDTO playerDTO;
    private IPlayer player;

    public ShowdownPlayerConstructorTest() {
        group = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
        playerDTO = new PlayerDTO(23, "Player A", 42, 15, 3, 2,
                group.getId(), group.getName());
    }

    @Before
    @Override
    public void setUp() {
        player = new ShowdownPlayer(playerDTO, group);
    }

    @Test
    public void testDTOConstructorPlayerId() {
        assertEquals(player.getId(), playerDTO.getId());
    }

    @Test
    public void testDTOConstructorGroupId() {
        assertEquals(player.getGroup().getId(), playerDTO.getGroupId());
    }

    @Test
    public void testDTOConstructorGroupName() {
        assertEquals(player.getGroup().getName(), playerDTO.getGroupName());
    }

    @Test
    public void testDTOConstructorPlayerName() {
        assertEquals(player.getName(), playerDTO.getName());
    }

    @Test
    public void testDTOConstructorBonuspoints() {
        assertEquals(player.getPoints(), playerDTO.getPoints());
    }

    @Test
    public void testDTOConstructorScoreRatio() {
        assertEquals(player.getScoreRatio(), playerDTO.getScoreRatio());
    }

    @Test
    public void testDTOConstructorWonGames() {
        assertEquals(player.getWonGames(), playerDTO.getWonGames());
    }

    @Test
    public void testDTOConstructorRank() {
        assertEquals(player.getRank(), playerDTO.getRank());
    }

}
