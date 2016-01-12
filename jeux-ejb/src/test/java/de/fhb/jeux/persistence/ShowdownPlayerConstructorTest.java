package de.fhb.jeux.persistence;

import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ShowdownPlayerConstructorTest {

    private IGroup group;
    private PlayerDTO playerDTO;
    private IPlayer player;

    public ShowdownPlayerConstructorTest() {
        group = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
        playerDTO = new PlayerDTO(23, "Player A", 42, 15, 3, 2,
                group.getId(), group.getName());
    }

    @Before
    public void setUp() {
        player = new ShowdownPlayer(playerDTO, group);
    }

    @Test
    public void dtoConstructorPlayerId() {
        assertEquals(player.getId(), playerDTO.getId());
    }

    @Test
    public void dtoConstructorGroupId() {
        assertEquals(player.getGroup().getId(), playerDTO.getGroupId());
    }

    @Test
    public void dtoConstructorGroupName() {
        assertEquals(player.getGroup().getName(), playerDTO.getGroupName());
    }

    @Test
    public void dtoConstructorPlayerName() {
        assertEquals(player.getName(), playerDTO.getName());
    }

    @Test
    public void dtoConstructorBonuspoints() {
        assertEquals(player.getPoints(), playerDTO.getPoints());
    }

    @Test
    public void dtoConstructorScoreRatio() {
        assertEquals(player.getScoreRatio(), playerDTO.getScoreRatio());
    }

    @Test
    public void dtoConstructorWonGames() {
        assertEquals(player.getWonGames(), playerDTO.getWonGames());
    }

    @Test
    public void dtoConstructorRank() {
        assertEquals(player.getRank(), playerDTO.getRank());
    }

}
