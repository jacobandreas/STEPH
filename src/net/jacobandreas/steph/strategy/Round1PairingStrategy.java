package net.jacobandreas.steph.strategy;

import net.jacobandreas.steph.tournament.*;
import java.util.*;

/**
 * Strategy to pair Round 1 at a tournament randomly (tournaments with challenge
 * or hand-picked Round 1 ranks should use a ManualPairingStrategy for the first
 * round instead).
 *
 * @author jacob
 */
public class Round1PairingStrategy extends BasePairingStrategy {

    /**
     * Creates a round, pairing the tournament teams randomly but making sure
     * to eliminate any impermissibles.
     * @param teams The teams to pair
     * @return A paired Round
     */
    public Round pair(ArrayList<Team> teams) {

        Round round = new Round();

        ArrayList<Team> shuffled = shuffle(teams);

        ArrayList<Team> pis = new ArrayList<Team>(Tournament.MAX_TEAMS/2);
        ArrayList<Team> deltas = new ArrayList<Team>(Tournament.MAX_TEAMS/2);

        while(!shuffled.isEmpty()) {

            int len = shuffled.size();

            // Get the last two teams from the stack
            Team team1 = shuffled.remove(len - 1);
            Team team2 = shuffled.remove(len - 2);

            // If they can't be paired against each other...
            while(!isPermitted(team1, team2)) {
                if(len > 2) {
                    // if we're not at the end of the stack, put one of the
                    // cards back and try another one
                    shuffled.add(0, team2);
                    team2 = shuffled.remove(len - 2);
                } else {
                    // if we are at the end of the stack, look through the list
                    // of already created pairings for a swap that will avoid
                    // any impermissibles.
                    boolean success = false;
                    for(int i = pis.size()-1; i >= 0; i--) {
                        if(isPermitted(team2, pis.get(i))
                           && isPermitted(team1, deltas.get(i))) {
                            Team temp = deltas.get(i);
                            deltas.set(i, team2);
                            team2 = temp;
                            success = true;
                            break;
                        }
                    }
                    assert success : "Sanity check failed: round 1 pairing impossible.";
                    // This should never happen if there are enough teams to actually
                    // hold a 4-round tournament
                }
            }

            pis.add(team1);
            deltas.add(team2);

        }

        // double check our pairings, then add them to the round
        for(int i = 0; i < pis.size(); i++) {
            Team team1 = pis.get(i);
            Team team2 = deltas.get(i);
            assert isPermitted(team1, team2) :
               "Sanity check failed: impermissibles paired (1)";
            Match m = new Match(team1, team2);
            round.addMatch(m);
        }

        return round;

    }

    // We don't need this
    @Override
    public Comparator<Swap> swapComparator() {
        return null;
    }
    
}
