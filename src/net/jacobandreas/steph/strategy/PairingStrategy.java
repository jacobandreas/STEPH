package net.jacobandreas.steph.strategy;

import net.jacobandreas.steph.event.PairingEventListener;
import net.jacobandreas.steph.exception.*;
import net.jacobandreas.steph.tournament.*;
import java.util.*;

public interface PairingStrategy {

    public Round pair(ArrayList<Team> teams) throws TournamentStateException;
    
    public void addPairingEventListener(PairingEventListener l);
    public PairingEventListener[] getPairingEventListeners();

}
