package de.fhb.jeux.session;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dao.PlayerDAO;
import de.fhb.jeux.dao.RankingDAO;
import de.fhb.jeux.dto.RankingDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRanking;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.logging.Logger;

@Stateless
public class FinalRankingBean implements FinalRankingLocal, FinalRankingRemote {

    protected static Logger logger = Logger.getLogger(FinalRankingBean.class);

    public FinalRankingBean() {
    }

    @EJB
    private RankingDAO rankingDAO;

    @EJB
    private PlayerDAO playerDAO;

    @EJB
    private GroupDAO groupDAO;

    @Override
    public List<RankingDTO> getRankingDTOs(IGroup group) {
        List<RankingDTO> rankings = rankingDAO.getRankingDTOs(group);
        if (!rankings.isEmpty()) {
            logger.debug(rankings);
        }
        return rankings;
    }

    @Override
    public void addRanking(IRanking ranking) {
        rankingDAO.addRanking(ranking);
    }
}
