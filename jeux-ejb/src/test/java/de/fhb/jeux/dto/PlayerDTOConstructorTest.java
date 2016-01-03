package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownGroup;
import de.fhb.jeux.persistence.ShowdownPlayer;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerDTOConstructorTest extends TestCase {

    private IGroup group;
    private PlayerDTO playerDTO;
    private IPlayer player;

    public PlayerDTOConstructorTest() {
        group = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
        player = new ShowdownPlayer(23, "Player A", 15, 7, 2, 1,
                (ShowdownGroup) group);
    }

    @Before
    @Override
    public void setUp() {
        playerDTO = new PlayerDTO(player);
    }

    @Test
    public void testConstructorPlayerId() {
        assertEquals(playerDTO.getId(), player.getId());
    }

    @Test
    public void testConstructorGroupId() {
        assertEquals(playerDTO.getGroupId(), player.getGroup().getId());
    }

    @Test
    public void testConstructorGroupName() {
        assertEquals(playerDTO.getGroupName(), player.getGroup().getName());
    }

    @Test
    public void testConstructorPlayerName() {
        assertEquals(playerDTO.getName(), player.getName());
    }

    @Test
    public void testConstructorBonuspoints() {
        assertEquals(playerDTO.getPoints(), player.getPoints());
    }

    @Test
    public void testConstructorScoreRatio() {
        assertEquals(playerDTO.getScoreRatio(), player.getScoreRatio());
    }

    @Test
    public void testConstructorWonGames() {
        assertEquals(playerDTO.getWonGames(), player.getWonGames());
    }

    @Test
    public void testConstructorRank() {
        assertEquals(playerDTO.getRank(), player.getRank());
    }
}
