package de.fhb.jeux.dao;

import de.fhb.jeux.dto.RankingDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRanking;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.jboss.logging.Logger;

@Stateless
@LocalBean
public class RankingDAO {

    private static Logger logger = Logger.getLogger(RankingDAO.class);

    @PersistenceContext(unitName = "JeuxEJB")
    private EntityManager em;

    public RankingDAO() {
    }

    public void addRanking(IRanking ranking) {
        em.persist(ranking);
        logger.debug("Persisted ranking " + ranking);
    }

    public List<IRanking> getRankings(IGroup group) {
        List<IRanking> rankings = new ArrayList<IRanking>();

        TypedQuery<IRanking> query = em.createNamedQuery("Ranking.findByGroup",
                IRanking.class);
        query.setParameter("group", group);
        rankings = query.getResultList();

        return rankings;
    }

    public List<RankingDTO> getRankingDTOs(IGroup group) {
        List<RankingDTO> rankingDTOs = new ArrayList<RankingDTO>();
        List<IRanking> rankings = getRankings(group);

        for (IRanking ranking : rankings) {
            // populate list of DTOs from Persistent Entities
            rankingDTOs.add(new RankingDTO(ranking));
        }

        return rankingDTOs;
    }
}
