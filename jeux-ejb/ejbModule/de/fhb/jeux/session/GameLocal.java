package de.fhb.jeux.session;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGroup;
import java.util.List;
import javax.ejb.Local;

@Local
public interface GameLocal {

    public List<GameDTO> getPlayedGameDTOsInGroup(IGroup group);

    public List<GameDTO> getUnplayedGameDTOsInGroup(IGroup group);

}
