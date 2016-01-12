package de.fhb.jeux.persistence;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShowdownGroupConstructorTest {

    private IGroup group;
    private GroupDTO groupDTO;

    public ShowdownGroupConstructorTest() {
        groupDTO = new GroupDTO(12, "Group A", 1, 2, 3, true, false);
    }

    @Before
    public void setUp() {
        group = new ShowdownGroup(groupDTO);
    }

    @Test
    public void dtoConstructorId() {
        assertEquals(group.getId(), groupDTO.getId());
    }

    @Test
    public void dtoConstructorName() {
        assertEquals(group.getName(), groupDTO.getName());
    }

    @Test
    public void dtoConstructorActive() {
        assertEquals(groupDTO.isActive(), group.isActive());
    }

    @Test
    public void dtoConstructorCompleted() {
        assertEquals(groupDTO.isCompleted(), group.isCompleted());
    }

    @Test
    public void dtoConstructorMinSets() {
        assertEquals(groupDTO.getMinSets(), group.getMinSets());
    }

    @Test
    public void dtoConstructorMaxSets() {
        assertEquals(groupDTO.getMaxSets(), group.getMaxSets());
    }

    @Test
    public void dtoConstructorRoundId() {
        assertEquals(groupDTO.getRoundId(), group.getRoundId());
    }

    @Test
    public void dtoConstructorHasGames() {
        assertEquals(groupDTO.hasGames(), group.hasGames());
    }

    @Test
    public void dtoConstructorSize() {
        assertEquals(groupDTO.getSize(), group.getSize());
    }

}
