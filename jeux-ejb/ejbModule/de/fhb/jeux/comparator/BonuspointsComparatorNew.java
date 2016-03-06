package de.fhb.jeux.comparator;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.model.IPlayer;
import java.util.Comparator;
import org.jboss.logging.Logger;

public class BonuspointsComparatorNew implements Comparator<IPlayer> {

    private static Logger logger = Logger
            .getLogger(BonuspointsComparatorNew.class);

    private PlayerDAO playerDAO;

    // poor man's injection
    public BonuspointsComparatorNew(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public int compare(IPlayer player1, IPlayer player2) {

        // first criterion: points aka bonus points, set points
        if (player1.getPoints() != player2.getPoints()) {
            if (player1.getPoints() > player2.getPoints()) {
                return -1;
            } else {
                return 1;
            }
        }

        // second criterion: won sets minus lost sets
        int player1SetDifference = player1.getWonSets() - player1.getLostSets();
        int player2SetDifference = player2.getWonSets() - player2.getLostSets();
        if (player1SetDifference != player2SetDifference) {
            if (player1SetDifference > player2SetDifference) {
                return -1;
            } else {
                return 1;
            }
        }

        // third criterion: score ratio
        if (player1.getScoreRatio() != player2.getScoreRatio()) {
            if (player1.getScoreRatio() > player2.getScoreRatio()) {
                return -1;
            } else {
                return 1;
            }
        }

        // fourth criterion: score
        if (player1.getScore() != player2.getScore()) {
            if (player1.getScore() > player2.getScore()) {
                return -1;
            } else {
                return 1;
            }
        }

        // direct comparison necessary...
        // must use IDs because we only have flat DTOs here;
        // also have to convert result to DTO
        IPlayer tempPlayer = playerDAO.directComparison(player1.getId(),
                player2.getId());
        if (tempPlayer != null) {
            if (player1.getId() == tempPlayer.getId()) {
                logger.info("Direct comparison won by " + player1);
                return -1;
            } else if (player2.getId() == tempPlayer.getId()) {
                logger.info("Direct comparison won by " + player2);
                return 1;
            }
        }

        logger.debug("Cannot determine higher-ranked player.");
        return 0;
    }
}
