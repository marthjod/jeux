package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.comparator.BonuspointsComparator;
import de.fhb.jeux.comparator.WonGamesComparator;
import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownPlayer;

@Stateless
@SuppressWarnings("ucd")
public class RankingBean implements RankingRemote, RankingLocal {

    protected static Logger logger = Logger.getLogger(RankingBean.class);

    public RankingBean() {
    }

    @EJB
    private PlayerDAO playerDAO;

    @Override
    public List<IPlayer> getRankedPlayers(IGroup group) {
        List<IPlayer> rankedPlayers = new ArrayList<IPlayer>();

        if (group != null) {
            // player entities
            List<ShowdownPlayer> players = group.getPlayers();

            if (!players.isEmpty()) {
                Comparator<IPlayer> comparator;
                // more than 1 set per game means we sort by bonus points first
                if (group.getMaxSets() > 1) {
                    comparator = new BonuspointsComparator(playerDAO);
                } else {
                    // 1 set per game: sort by won games, then score ratio
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
                int rank = 0;
                IPlayer temp = null;
                logger.debug("Ranking " + group.getName() + ":");
                while (sortedPlayers.peek() != null) {
                    temp = sortedPlayers.poll();
                    temp.setRank(++rank);
                    logger.debug(temp.getRank() + ". " + temp.getName());
                    rankedPlayers.add(temp);
                }
            }
        }

        return rankedPlayers;
    }

    // DRY
    @Override
    public List<PlayerDTO> getRankedPlayerDTOs(IGroup group) {

        // return at least an empty, but initialized list
        List<PlayerDTO> rankedPlayerDTOs = new ArrayList<PlayerDTO>();

        for (IPlayer player : getRankedPlayers(group)) {
            rankedPlayerDTOs.add(new PlayerDTO(player));
        }

        return rankedPlayerDTOs;
    }
}
