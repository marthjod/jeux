package de.fhb.jeux.session;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.model.IGroup;
import java.util.List;
import javax.ejb.Local;
import javax.servlet.ServletContext;

@Local
public interface CalcGamesLocal {

    public int writeGamesForGroup(IGroup group, boolean shuffledMode);

    public String getShuffledGamesList(IGroup group, String format,
            ServletContext sc);

    public List<GameDTO> calcGameDTOsForGroup(IGroup group, boolean shuffledMode);

}
