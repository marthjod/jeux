package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownGroup;
import de.fhb.jeux.persistence.ShowdownPlayer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerDTOConstructorTest {

    private IGroup group;
    private PlayerDTO playerDTO;
    private IPlayer player;

    public PlayerDTOConstructorTest() {
        group = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
        player = new ShowdownPlayer(23, "Player A", 15, 7, 2, 1,
                (ShowdownGroup) group);
    }

    @Before
    public void setUp() {
        playerDTO = new PlayerDTO(player);
    }

    @Test
    public void constructorPlayerId() {
        assertEquals(playerDTO.getId(), player.getId());
    }

    @Test
    public void constructorGroupId() {
        assertEquals(playerDTO.getGroupId(), player.getGroup().getId());
    }

    @Test
    public void constructorGroupName() {
        assertEquals(playerDTO.getGroupName(), player.getGroup().getName());
    }

    @Test
    public void constructorPlayerName() {
        assertEquals(playerDTO.getName(), player.getName());
    }

    @Test
    public void constructorBonuspoints() {
        assertEquals(playerDTO.getPoints(), player.getPoints());
    }

    @Test
    public void constructorScoreRatio() {
        assertEquals(playerDTO.getScoreRatio(), player.getScoreRatio());
    }

    @Test
    public void constructorWonGames() {
        assertEquals(playerDTO.getWonGames(), player.getWonGames());
    }

    @Test
    public void constructorRank() {
        assertEquals(playerDTO.getRank(), player.getRank());
    }
}
