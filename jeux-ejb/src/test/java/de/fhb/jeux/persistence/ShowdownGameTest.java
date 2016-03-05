package de.fhb.jeux.persistence;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IPlayer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShowdownGameTest {

    private IPlayer player1;
    private IPlayer player2;
    private IGame game;

    public ShowdownGameTest() {

    }

    @Before
    public void setUp() {
        player1 = new ShowdownPlayer();
        player2 = new ShowdownPlayer();
        game = new ShowdownGame(new ShowdownGroup(), player1, player2);
    }

    @Test
    public void getLoser() {
        game.setWinner(player1);
        assertEquals(game.getLoser(), player2);
    }

    @Test
    public void getWinner() {
        game.setWinner(player1);
        assertEquals(game.getWinner(), player1);
    }

}
