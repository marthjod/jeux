package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GameSetDAO;
import de.fhb.jeux.model.IGameSet;

@Stateless
public class GameSetBean implements GameSetRemote, GameSetLocal {

	@EJB
	private GameSetDAO gameSetDAO;
	
    public GameSetBean() {
    }
    
    public List<IGameSet> getAllGameSets() {
		return gameSetDAO.getAllGameSets();
	}

}
