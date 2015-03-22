package de.fhb.jeux.session;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IPlayer;
import java.util.List;
import javax.ejb.Remote;

@Remote
@SuppressWarnings("ucd")
public interface PlayerRemote {

    @SuppressWarnings("ucd")
    public IPlayer getPlayerById(int playerId);

    public List<GameDTO> getPlayedGames(IPlayer player);
}
