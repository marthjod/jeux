package de.fhb.jeux.session;

import de.fhb.jeux.model.IGame;

import de.fhb.jeux.config.BonusPointsDistribution;
import de.fhb.jeux.config.BonusPointsRule;
import de.fhb.jeux.model.IPlayer;

import de.fhb.jeux.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateGameBeanTest {

    private IGame game;

    private ShowdownGameSet set1;
    private ShowdownGameSet set2;
    private ShowdownGameSet set3;
    private List<ShowdownGameSet> sets;
    private UpdateGameBean bean;

    private BonusPointsDistribution config;
    private List<BonusPointsRule> ruleList;
    private ShowdownGroup group;
    private IPlayer player1;
    private IPlayer player2;

    public UpdateGameBeanTest() {
        bean = new UpdateGameBean();
        ruleList = new ArrayList<>();
        ruleList.add(new BonusPointsRule(2, 3, 3, 1));
        ruleList.add(new BonusPointsRule(2, 2, 4, 0));
        config = new BonusPointsDistribution(ruleList);
    }

    @Before
    public void setUp() {
        group = new ShowdownGroup(1, "Group A", 1, 2, 3, false, false);

        player1 = new ShowdownPlayer(11, "Player A.1", 4, 22, 1, 1, group);
        player2 = new ShowdownPlayer(12, "Player A.2", 0, -22, 2, 0, group);

        game = new ShowdownGame(group, player1, player2);

        set1 = new ShowdownGameSet(game, 1);
        set2 = new ShowdownGameSet(game, 2);
        set3 = new ShowdownGameSet(game, 3);

        set1.setPlayer1Score(11);
        set2.setPlayer2Score(0);
        set1.setWinner(player1);

        set2.setPlayer1Score(11);
        set2.setPlayer2Score(0);
        set2.setWinner(player1);

        sets = new ArrayList<>();

        sets.add(set1);
        sets.add(set2);
        sets.add(set3);
        for (ShowdownGameSet set : sets) {
            set.setGame(game);
        }

        game.setSets(sets);
        game.setWinner(player1);
    }

    @Test
    public void clearGameResetsWinner() {
        game = bean.clearGame(game, config);
        assertEquals(null, game.getWinner());
        assertEquals(null, game.getLoser());
        assertEquals(false, game.hasWinner());
        for (ShowdownGameSet set : game.getSets()) {
            assertEquals(null, set.getWinner());
            assertEquals(false, set.hasWinner());
            assertEquals(true, set.isUnplayed());
        }
    }

    @Test
    public void clearGameResetsWonGames() {
        game = bean.clearGame(game, config);
        assertEquals(0, game.getPlayer1().getWonGames());
        assertEquals(0, game.getPlayer2().getWonGames());
    }

    @Test
    public void clearGamesResetsPoints() {
        game = bean.clearGame(game, config);
        assertEquals(0, game.getPlayer1().getPoints());
        assertEquals(0, game.getPlayer2().getPoints());
    }

    @Test
    public void clearGamesResetsScoreRatio() {
        game = bean.clearGame(game, config);
        for (ShowdownGameSet set : game.getSets()) {
            assertEquals(0, set.getPlayer1Score());
            assertEquals(0, set.getPlayer2Score());
        }
        for (IPlayer player : game.getPlayers()) {
            assertEquals(0, player.getScoreRatio());
        }

    }
}
