package de.fhb.jeux.comparator;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownGroup;
import de.fhb.jeux.persistence.ShowdownPlayer;
import java.util.Comparator;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class WonGamesComparatorTest extends TestCase {

    private static final int PLAYER_1_WINS = -1;
    private static final int PLAYER_2_WINS = 1;
    private static final int NO_WINNER = 0;

    private PlayerDAO playerDAO;
    private Comparator comparator;
    private IPlayer player1;
    private IPlayer player2;
    private ShowdownGroup group;

    public WonGamesComparatorTest() {
        playerDAO = mock(PlayerDAO.class);
        group = mock(ShowdownGroup.class);
        comparator = new WonGamesComparator(playerDAO);
        player1 = new ShowdownPlayer(1, "Player 1", 4, 5, 0, 2, group);
        player2 = new ShowdownPlayer(2, "Player 2", 4, 5, 0, 2, group);
    }

    @Before
    @Override
    public void setUp() {
        when(playerDAO.getPlayerById(1)).thenReturn(player1);
        when(playerDAO.getPlayerById(2)).thenReturn(player2);
    }

    @Test
    public void testComparePlayer1WinsByGames() {
        player1.setWonGames(player2.getWonGames() + 1);
        assertEquals(PLAYER_1_WINS, comparator.compare(player1, player2));
    }

    @Test
    public void testComparePlayer2WinsByGames() {
        player2.setWonGames(player1.getWonGames() + 1);
        assertEquals(PLAYER_2_WINS, comparator.compare(player1, player2));
    }

    @Test
    public void testComparePlayer1WinsByScoreRatio() {
        player1.setScoreRatio(player2.getScoreRatio() + 1);
        assertEquals(PLAYER_1_WINS, comparator.compare(player1, player2));
    }

    @Test
    public void testComparePlayer2WinsByScoreRatio() {
        player2.setScoreRatio(player1.getScoreRatio() + 1);
        assertEquals(PLAYER_2_WINS, comparator.compare(player1, player2));
    }

    @Test
    public void testComparePlayer1WinsByDirectComparison() {
        when(playerDAO.directComparison(player1.getId(), player2.getId())).
                thenReturn(player1);
        assertEquals(PLAYER_1_WINS, comparator.compare(player1, player2));
    }

    @Test
    public void testComparePlayer2WinsByDirectComparison() {
        when(playerDAO.directComparison(player1.getId(), player2.getId())).
                thenReturn(player2);
        assertEquals(PLAYER_2_WINS, comparator.compare(player1, player2));
    }

    @Test
    public void testCompareNoWinner() {
        when(playerDAO.directComparison(player1.getId(), player2.getId())).
                thenReturn(null);
        assertEquals(NO_WINNER, comparator.compare(player1, player2));
    }

}
