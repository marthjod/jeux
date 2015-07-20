package de.fhb.jeux.session;

import de.fhb.jeux.model.IPlayer;
import javax.ejb.Remote;

@Remote
public interface UpdatePlayerRemote {

    public void updatePlayer(IPlayer player);
}
