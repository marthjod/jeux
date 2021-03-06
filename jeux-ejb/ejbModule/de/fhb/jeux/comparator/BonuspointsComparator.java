package de.fhb.jeux.comparator;

import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.model.IPlayer;
import java.util.Comparator;
import org.jboss.logging.Logger;

public class BonuspointsComparator implements Comparator<IPlayer> {

    private static Logger logger = Logger
            .getLogger(BonuspointsComparator.class);

    private PlayerDAO playerDAO;

    // poor man's injection
    public BonuspointsComparator(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public int compare(IPlayer p1, IPlayer p2) {
        IPlayer higherRankedPlayer = null;

        if (p1.getPoints() > p2.getPoints()) {
            higherRankedPlayer = p1;
        } else if (p1.getPoints() < p2.getPoints()) {
            higherRankedPlayer = p2;
        } else {
            // points are equal;
            // next up: score ratio
            if (p1.getScoreRatio() > p2.getScoreRatio()) {
                higherRankedPlayer = p1;
            } else if (p1.getScoreRatio() < p2.getScoreRatio()) {
                higherRankedPlayer = p2;
            } else {
                // direct comparison necessary...
                // must use IDs because we only have flat DTOs here;
                // also have to convert result to DTO
                IPlayer tempPlayer = playerDAO.directComparison(p1.getId(),
                        p2.getId());
                if (tempPlayer != null) {
                    if (p1.getId() == tempPlayer.getId()) {
                        higherRankedPlayer = p1;
                    } else if (p2.getId() == tempPlayer.getId()) {
                        higherRankedPlayer = p2;
                    }

                    if (higherRankedPlayer != null) {
                        logger.info("Direct comparison won by "
                                + higherRankedPlayer.getName());
                    }
                }
            }
        }

        if (higherRankedPlayer != null) {
            if (p1.equals(higherRankedPlayer)) {
                return -1;
            } else if (p2.equals(higherRankedPlayer)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            logger.debug("Cannot determine higher-ranked player.");
            return 0;
        }
    }
}
