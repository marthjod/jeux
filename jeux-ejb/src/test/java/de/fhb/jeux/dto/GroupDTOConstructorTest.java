package de.fhb.jeux.dto;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownGroup;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class GroupDTOConstructorTest extends TestCase {

    private IGroup group;
    private GroupDTO groupDTO;

    public GroupDTOConstructorTest() {
        group = new ShowdownGroup(12, "Group A", 1, 2, 3, true, false);
    }

    @Before
    @Override
    public void setUp() {
        groupDTO = new GroupDTO(group);
    }

    @Test
    public void testConstructorId() {
        assertEquals(groupDTO.getId(), group.getId());
    }

    @Test
    public void testConstructorName() {
        assertEquals(groupDTO.getName(), group.getName());
    }

    @Test
    public void testConstructorActive() {
        assertEquals(groupDTO.isActive(), group.isActive());
    }

    @Test
    public void testConstructorCompleted() {
        assertEquals(groupDTO.isCompleted(), group.isCompleted());
    }

    @Test
    public void testConstructorMinSets() {
        assertEquals(groupDTO.getMinSets(), group.getMinSets());
    }

    @Test
    public void testConstructorMaxSets() {
        assertEquals(groupDTO.getMaxSets(), group.getMaxSets());
    }

    @Test
    public void testConstructorRoundId() {
        assertEquals(groupDTO.getRoundId(), group.getRoundId());
    }

    @Test
    public void testConstructorHasGames() {
        assertEquals(groupDTO.hasGames(), group.hasGames());
    }

    @Test
    public void testConstructorSize() {
        assertEquals(groupDTO.getSize(), group.getSize());
    }

}
