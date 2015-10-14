package de.fhb.jeux.session;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGame;
import de.fhb.jeux.model.IPlayer;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless

public class PlayerBean implements PlayerRemote, PlayerLocal {

    @EJB
    private PlayerDAO playerDAO;

    public PlayerBean() {
    }

    @Override
    public IPlayer getPlayerById(int playerId) {
        return playerDAO.getPlayerById(playerId);
    }

    @Override
    public List<GameDTO> getPlayedGames(IPlayer player) {

        List<IGame> playedGames = playerDAO.getPlayedGames(player);
        List<GameDTO> playedGameDTOs = new ArrayList<GameDTO>();

        for (IGame playedGame : playedGames) {
            playedGameDTOs.add(new GameDTO(playedGame));
        }

        return playedGameDTOs;
    }

    @Override
    public Long getCountPlayedGames(IPlayer player) {
        return playerDAO.getCountPlayedGames(player);
    }

    @Override
    public Long getCountWonGames(IPlayer player) {
        return playerDAO.getCountWonGames(player);
    }
}
