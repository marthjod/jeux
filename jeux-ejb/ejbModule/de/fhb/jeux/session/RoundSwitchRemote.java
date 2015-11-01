package de.fhb.jeux.session;

import javax.ejb.Remote;

@Remote
public interface RoundSwitchRemote {

    public boolean currentRoundFinished();

    public boolean switchRound();
}
