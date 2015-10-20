package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownPlayer;

@Stateless

public class CreatePlayerBean implements CreatePlayerRemote, CreatePlayerLocal {

    protected static Logger logger = Logger.getLogger(CreatePlayerBean.class);

    @EJB
    private PlayerDAO playerDAO;

    public CreatePlayerBean() {
    }

    @Override
    public boolean createPlayer(PlayerDTO playerDTO, IGroup group) {

        boolean checkOK = false;
        boolean created = false;

        // rank, points, score ratio, won games must be 0 initially
        if (playerDTO != null && playerDTO.getRank() == 0
                && playerDTO.getScoreRatio() == 0
                && playerDTO.getPoints() == 0
                && playerDTO.getWonGames() == 0) {
            checkOK = true;
        }

        // TODO how to handle same names? #business-logic
        // TODO check if group exists #business-logic
        if (checkOK) {
            // persist after converting
            IPlayer newPlayer = new ShowdownPlayer(playerDTO, group);
            playerDAO.addPlayer(newPlayer);
            created = true;
            logger.debug("Added player '" + newPlayer.getName() + "'");
        }

        return created;
    }
}
