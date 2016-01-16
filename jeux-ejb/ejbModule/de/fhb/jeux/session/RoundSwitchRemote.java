package de.fhb.jeux.session;

import de.fhb.jeux.model.IGroup;
import javax.ejb.Remote;

@Remote
public interface RoundSwitchRemote {

    public int doTakeover(IGroup destGroup);
}
