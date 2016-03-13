package de.fhb.jeux.session;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.dao.RankingDAO;
import de.fhb.jeux.dto.RankingDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.model.IRanking;
import de.fhb.jeux.persistence.ShowdownPlayer;
import de.fhb.jeux.persistence.ShowdownRanking;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.logging.Logger;

@Stateless
public class FinalRankingBean implements FinalRankingLocal, FinalRankingRemote {

    protected static Logger logger = Logger.getLogger(FinalRankingBean.class);

    public FinalRankingBean() {
    }

    // tests
    public FinalRankingBean(RankingDAO rankingDAO) {
        this.rankingDAO = rankingDAO;
    }

    @EJB
    private RankingDAO rankingDAO;

    @EJB
    private PlayerDAO playerDAO;

    @EJB
    private GroupDAO groupDAO;

    @Override
    public List<IRanking> getRankings(IGroup group) {
        List<IRanking> rankings = rankingDAO.getRankings(group);
        if (!rankings.isEmpty()) {
            logger.debug(rankings);
        }
        return rankings;
    }

    @Override
    public List<RankingDTO> getRankingDTOs(IGroup group) {
        List<RankingDTO> rankings = rankingDAO.getRankingDTOs(group);
        if (!rankings.isEmpty()) {
            logger.debug(rankings);
        }
        return rankings;
    }

    @Override
    public List<ShowdownPlayer> getRankedPlayers(IGroup group) {
        List<ShowdownPlayer> rankedPlayers = new ArrayList<>();

        for (IRanking ranking : getRankings(group)) {
            rankedPlayers.add((ShowdownPlayer) ranking.getPlayer());
        }

        if (!rankedPlayers.isEmpty()) {
            logger.debug(rankedPlayers);
        }
        return rankedPlayers;
    }

    @Override
    public void addRanking(IRanking ranking) {
        rankingDAO.addRanking(ranking);
    }
}
