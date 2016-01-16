package de.fhb.jeux.session;

import de.fhb.jeux.model.IGroup;
import javax.ejb.Local;

@Local
public interface RoundSwitchLocal {

    public int doTakeover(IGroup destGroup);

}
