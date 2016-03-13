package de.fhb.jeux.session;

import de.fhb.jeux.dto.RankingDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.model.IRanking;
import de.fhb.jeux.persistence.ShowdownPlayer;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface FinalRankingRemote {

    public List<IRanking> getRankings(IGroup group);

    public List<RankingDTO> getRankingDTOs(IGroup group);

    public List<ShowdownPlayer> getRankedPlayers(IGroup group);

    public void addRanking(IRanking ranking);
}
