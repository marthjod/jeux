package de.fhb.jeux.persistence;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class ShowdownGroupConstructorTest extends TestCase {

    private IGroup group;
    private GroupDTO groupDTO;

    public ShowdownGroupConstructorTest() {
        groupDTO = new GroupDTO(12, "Group A", 1, 2, 3, true, false);
    }

    @Before
    @Override
    public void setUp() {
        group = new ShowdownGroup(groupDTO);
    }

    @Test
    public void testDTOConstructorId() {
        assertEquals(group.getId(), groupDTO.getId());
    }

    @Test
    public void testDTOConstructorName() {
        assertEquals(group.getName(), groupDTO.getName());
    }

    @Test
    public void testDTOConstructorActive() {
        assertEquals(groupDTO.isActive(), group.isActive());
    }

    @Test
    public void testDTOConstructorCompleted() {
        assertEquals(groupDTO.isCompleted(), group.isCompleted());
    }

    @Test
    public void testDTOConstructorMinSets() {
        assertEquals(groupDTO.getMinSets(), group.getMinSets());
    }

    @Test
    public void testDTOConstructorMaxSets() {
        assertEquals(groupDTO.getMaxSets(), group.getMaxSets());
    }

    @Test
    public void testDTOConstructorRoundId() {
        assertEquals(groupDTO.getRoundId(), group.getRoundId());
    }

    @Test
    public void testDTOConstructorHasGames() {
        assertEquals(groupDTO.hasGames(), group.hasGames());
    }

    @Test
    public void testDTOConstructorSize() {
        assertEquals(groupDTO.getSize(), group.getSize());
    }

}
