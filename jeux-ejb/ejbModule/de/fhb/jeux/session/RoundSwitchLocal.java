package de.fhb.jeux.session;

import javax.ejb.Local;

@Local
public interface RoundSwitchLocal {

    public boolean currentRoundFinished();

    public boolean switchRound();

}
