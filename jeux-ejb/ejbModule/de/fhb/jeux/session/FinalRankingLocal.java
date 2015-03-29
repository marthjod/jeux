package de.fhb.jeux.session;

import de.fhb.jeux.dto.RankingDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRanking;
import java.util.List;
import javax.ejb.Local;

@Local
public interface FinalRankingLocal {

    public List<RankingDTO> getRankingDTOs(IGroup group);

    public void addRanking(IRanking ranking);
}
