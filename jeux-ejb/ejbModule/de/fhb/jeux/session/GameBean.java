package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IGroup;

@Stateless
@SuppressWarnings("ucd")
public class GameBean implements GameRemote, GameLocal {

    @EJB
    private GameDAO gameDAO;

    public GameBean() {
    }

    @Override
    public List<GameDTO> getPlayedGameDTOsInGroup(IGroup group) {
        List<IGame> playedGames = gameDAO.getPlayedGamesInGroup(group);
        List<GameDTO> playedGameDTOs = new ArrayList<GameDTO>();

        for (IGame playedGame : playedGames) {
            playedGameDTOs.add(new GameDTO(playedGame));
        }

        return playedGameDTOs;
    }

    @Override
    public List<GameDTO> getUnplayedGameDTOsInGroup(IGroup group) {
        List<IGame> unplayedGames = gameDAO.getUnplayedGamesInGroup(group);
        List<GameDTO> unplayedGameDTOs = new ArrayList<GameDTO>();

        for (IGame unplayedGame : unplayedGames) {
            unplayedGameDTOs.add(new GameDTO(unplayedGame));
        }

        return unplayedGameDTOs;
    }

}
