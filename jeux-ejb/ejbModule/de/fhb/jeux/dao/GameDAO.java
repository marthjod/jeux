package de.fhb.jeux.dao;

import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownGame;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.jboss.logging.Logger;

@Stateless
@LocalBean
public class GameDAO {

    private static Logger logger = Logger.getLogger(GameDAO.class);

    @PersistenceContext(unitName = "JeuxEJB")
    private EntityManager em;

    @EJB
    private GameSetDAO gameSetDAO;

    public GameDAO() {
    }

    public void addGame(IGame game) {
        em.persist(game);
        logger.debug("Persisted game " + game);
    }

    public void updateGame(IGame game) {
        em.merge(game);
    }

    public Long getCountPlayedGamesInGroup(IGroup group) {
        Long count = 0L;
        Query query = em.createNamedQuery("Game.findCountPlayedInGroup");
        query.setParameter("group", group);
        Object res = query.getSingleResult();
        count = (Long) res;
        return count;
    }

    public List<IGame> getPlayedGamesInGroup(IGroup group) {
        List<IGame> playedGames = new ArrayList<IGame>();

        TypedQuery<IGame> query = em.createNamedQuery("Game.findPlayedInGroup",
                IGame.class);
        query.setParameter("group", group);
        playedGames = query.getResultList();

        return playedGames;
    }

    public List<IGame> getUnplayedGamesInGroup(IGroup group) {
        List<IGame> unplayedGames = new ArrayList<IGame>();

        TypedQuery<IGame> query = em.createNamedQuery(
                "Game.findUnplayedInGroup", IGame.class);
        query.setParameter("group", group);
        unplayedGames = query.getResultList();

        return unplayedGames;
    }

    public List<IGame> getGamesInGroup(IGroup group) {
        List<IGame> games = new ArrayList<IGame>();

        TypedQuery<IGame> query = em.createNamedQuery("Game.findAllInGroup",
                IGame.class);
        query.setParameter("group", group);
        games = query.getResultList();

        return games;
    }

    // for deletion and updates
    public IGame getGameById(int gameId) {
        IGame game = new ShowdownGame();
        TypedQuery<IGame> query = em.createNamedQuery("Game.findById",
                IGame.class);
        query.setParameter("id", gameId);
        game = query.getSingleResult();
        return game;
    }
}
