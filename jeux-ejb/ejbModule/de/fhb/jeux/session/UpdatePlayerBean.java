package de.fhb.jeux.session;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.model.IPlayer;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class UpdatePlayerBean implements UpdatePlayerLocal, UpdatePlayerRemote {

    @EJB
    private PlayerDAO playerDAO;

    @Override
    public void updatePlayer(IPlayer player) {
        playerDAO.updatePlayer(player);
    }
}
