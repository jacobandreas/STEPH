package net.jacobandreas.steph.tournament;

import java.util.*;
import net.jacobandreas.steph.exception.*;

/**
 * Represents one round of a Tournament.
 * @author jacob
 */
public class Round {

    private boolean pairingLocked;

    private ArrayList<Match> matches;

    private boolean played;

    /**
     * Creates a new round with no matches in it.
     */
    public Round() {
        pairingLocked = false;
        matches = new ArrayList<Match>(Tournament.MAX_TEAMS);
        played = false;
    }

    /**
     * Adds a match to the round.
     */
    public void addMatch(Match m) {
        matches.add(m);
    }

    /**
     * Removes a match from the round.
     */
    public void removeMatch(Match m) {
        matches.remove(m);
    }

    /**
     * Gets the index of a match within the round.
     */
    public int indexOfMatch(Match m) {
        return matches.indexOf(m);
    }

    /**
     * Gets a list of all the matches in the round.
     */
    public List<Match> getMatches() { return Collections.unmodifiableList(matches); }

    /**
     * Gets the number of matches in the round.
     */
    public int getNumMatches() { return matches.size(); }

    /**
     * Determines whether the pairing for this round has been locked.
     */
    public boolean isPairingLocked() {
        return pairingLocked;
    }

    /**
     * Sets the lock on this round's pairing.
     */
    public void setPairingLocked(boolean pairingLocked) throws TournamentStateException {
        if(this.pairingLocked) {
            // we can't modify the lock after it has already been set
            throw new TournamentStateException("Attempting to set lock on a previously locked round!");
        }
        this.pairingLocked = pairingLocked;
    }

    /**
     * Plays each of the round's matches in turn
     */
    public void play() {
        for(Match m : matches) {
            m.play();
        }
        played = true;
    }

    /**
     * Determines whether this round has been played
     */
    public boolean isPlayed() {
        return played;
    }

    @Override
    public String toString() {
        String str = "";
        for(Match m : matches) {
            str += m + "\n";
        }
        str = str.trim();
        return str;
    }

}
