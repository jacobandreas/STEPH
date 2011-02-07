package net.jacobandreas.steph.strategy;

import net.jacobandreas.steph.exception.*;
import net.jacobandreas.steph.event.*;
import net.jacobandreas.steph.tournament.*;
import java.util.*;

/**
 * Allows the user to manually assign the pairings for this round (useful for
 * tournaments with challenges, and for double-checking old tournaments).
 *
 * Rather than automatically producing a set of pairings, this strategy accepts
 * a list of externally determined matches to be added to the next round. It
 * will make sure that this list is complete (i.e. that it contains all of the
 * necessary teams) and consistent (no extraneous teams), but it will NOT
 * verify that the provided matches are permissible.
 *
 * @author jacob
 */
public class ManualPairingStrategy implements PairingStrategy {

    private ArrayList<PairingEventListener> listeners;
    private Round round;

    /**
     * Creates a new ManualPairingStrategy with an empty list of matches.
     */
    public ManualPairingStrategy() {
        listeners = new ArrayList<PairingEventListener>();
        round = new Round();
    }

    /**
     * Creates a new ManualPairingStrategy that will pair using the given set
     * of matches.
     * @param matches The matches to pair the next round with.
     */
    public ManualPairingStrategy(Collection<Match> matches) {
        this();
        for(Match m : matches) {
            round.addMatch(m);
        }
    }

    /**
     * Generates a pairing for this round, using the matches provided by the user.
     * @param teams The teams to pair
     * @return A pairing for the next round
     * @throws net.jacobandreas.steph.exception.TournamentStateException if the
     * provided set of matches is incomplete or invalid.
     */
    public Round pair(ArrayList<Team> teams) throws TournamentStateException {
        Collection<Team> roundTeams = getTeams();
        if(!(roundTeams.containsAll(teams) && teams.containsAll(roundTeams))) {
            throw new TournamentStateException("Tournament teams and round " +
                    "teams don't match");
        }
        return round;
    }

    /**
     * Gets the teams the currently entered set of matches comprises.
     * @return the teams this strategy knows about
     */
    public Collection<Team> getTeams() {
        ArrayList<Team> roundTeams = new ArrayList<Team>();
        for(Match m : round.getMatches()) {
            roundTeams.add(m.getPi());
            roundTeams.add(m.getDelta());
        }
        return Collections.unmodifiableCollection(roundTeams);
    }

    /**
     * Gets the set of matches this strategy will attempt to pair
     * @return The list of matches
     */
    public List<Match> getMatches() {
        return round.getMatches();
    }

    /**
     * Adds a new match to the list of matches this strategy will try to pair
     * @param m the match to add
     */
    public void addMatch(Match m) {
        round.addMatch(m);
    }

    /**
     * Removes a match from the list of matches this strategy will try to pair.
     * @param m The match to remove
     */
    public void removeMatch(Match m) {
        round.removeMatch(m);
    }

    /**
     * Gets the number of matches this strategy knows about.
     * @return the number of added matches
     */
    public int getNumMatches() {
        return round.getNumMatches();
    }

    /**
     * Registers a new PairingEventListener
     * @param l the listener to register
     */
    public void addPairingEventListener(PairingEventListener l) {
        listeners.add(l);
    }

    /**
     * Gets a list of registered listeners
     * @return an array containing all the listeners
     */
    public PairingEventListener[] getPairingEventListeners() {
        return listeners.toArray(new PairingEventListener[0]);
    }

}
