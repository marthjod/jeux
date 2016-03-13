package de.fhb.jeux.session;

import de.fhb.jeux.comparator.BonuspointsComparator;
import de.fhb.jeux.comparator.WonGamesComparator;
import de.fhb.jeux.dao.GameDAO;
import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownPlayer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.logging.Logger;

@Stateless
public class AdHocRankingBean implements AdHocRankingRemote, AdHocRankingLocal {

    protected static Logger logger = Logger.getLogger(AdHocRankingBean.class);

    public AdHocRankingBean() {
    }

    // tests
    public AdHocRankingBean(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    @EJB
    private PlayerDAO playerDAO;

    @EJB
    private GameDAO gameDAO;

    @Override
    public List<IPlayer> getRankedPlayers(IGroup group) {
        List<IPlayer> rankings = new ArrayList<>();
        int rank = 0;

        if (group != null) {
            // player entities
            List<ShowdownPlayer> players = group.getPlayers();

            if (!players.isEmpty()) {

                // only calculate if games have been actually played
                if (gameDAO.getCountPlayedGamesInGroup(group) > 0) {
                    Comparator<IPlayer> comparator;
                    // best-of-3: sort by bonus points first
                    if (group.getMaxSets() == 3) {
                        comparator = new BonuspointsComparator(playerDAO);
                    } else {
                        // best-of-1, best-of-5, best-of-7, etc.:
                        // sort by won games first, then score ratio
                        comparator = new WonGamesComparator(playerDAO);
                    }

                    // used for sorting
                    PriorityQueue<IPlayer> sortedPlayers = new PriorityQueue<IPlayer>(
                            5, comparator);

                    // go thru player entities, convert them and add to queue
                    // which maintains order defined by comparator
                    for (IPlayer player : players) {
                        sortedPlayers.add(player);
                    }

                    // use "rank" field for (ephemeral) ranking info
                    // so no one has to guess whether returned list is sorted or not
                    // and in what manner
                    IPlayer temp = null;
                    while (sortedPlayers.peek() != null) {
                        temp = sortedPlayers.poll();
                        temp.setRank(++rank);
                        // logger.debug(temp.getRank() + ". " + temp.getName());
                        rankings.add(temp);
                    }
                } else {
                    // no games won yet --
                    // apply arbitrary ranking, skipping comparators etc.
                    logger.debug("Skipped ranking calculation, applying arbitrary ranks");
                    for (IPlayer temp : group.getPlayers()) {
                        temp.setRank(++rank);
                        rankings.add(temp);
                    }
                }

                if (!rankings.isEmpty()) {
                    logger.debug(rankings);
                }
            } else {
                logger.warn("No players in group " + group);
            }
        } else {
            logger.error("Group is null");
        }
        return rankings;
    }

    // DRY
    @Override
    public List<PlayerDTO> getRankedPlayerDTOs(IGroup group) {

        // return at least an empty, but initialized list
        List<PlayerDTO> rankedPlayerDTOs = new ArrayList<>();

        for (IPlayer player : getRankedPlayers(group)) {
            rankedPlayerDTOs.add(new PlayerDTO(player));
        }

        return rankedPlayerDTOs;
    }
}
