package de.fhb.jeux.session;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IPlayer;
import java.util.List;
import javax.ejb.Remote;

@Remote

public interface PlayerRemote {

    public IPlayer getPlayerById(int playerId);

    public List<GameDTO> getPlayedGames(IPlayer player);

    public Long getCountPlayedGames(IPlayer player);

    public Long getCountWonGames(IPlayer player);

    public List<PlayerDTO> getAllPlayerDTOs();
}
