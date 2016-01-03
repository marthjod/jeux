package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownGroup;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GroupDTOConstructorTest {

    private IGroup group;
    private GroupDTO groupDTO;

    public GroupDTOConstructorTest() {
        group = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
    }

    @Before
    public void setUp() {
        groupDTO = new GroupDTO(group);
    }

    @Test
    public void constructorId() {
        assertEquals(groupDTO.getId(), group.getId());
    }

    @Test
    public void constructorName() {
        assertEquals(groupDTO.getName(), group.getName());
    }

    @Test
    public void constructorActive() {
        assertEquals(groupDTO.isActive(), group.isActive());
    }

    @Test
    public void constructorCompleted() {
        assertEquals(groupDTO.isCompleted(), group.isCompleted());
    }

    @Test
    public void constructorMinSets() {
        assertEquals(groupDTO.getMinSets(), group.getMinSets());
    }

    @Test
    public void constructorMaxSets() {
        assertEquals(groupDTO.getMaxSets(), group.getMaxSets());
    }

    @Test
    public void constructorRoundId() {
        assertEquals(groupDTO.getRoundId(), group.getRoundId());
    }

    @Test
    public void constructorHasGames() {
        assertEquals(groupDTO.hasGames(), group.hasGames());
    }

    @Test
    public void constructorSize() {
        assertEquals(groupDTO.getSize(), group.getSize());
    }

}
