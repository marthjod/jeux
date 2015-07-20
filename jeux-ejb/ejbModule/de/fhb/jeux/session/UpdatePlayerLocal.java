package de.fhb.jeux.session;

import de.fhb.jeux.model.IPlayer;
import javax.ejb.Local;

@Local
public interface UpdatePlayerLocal {

    public void updatePlayer(IPlayer player);
}
