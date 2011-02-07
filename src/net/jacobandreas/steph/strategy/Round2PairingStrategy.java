package net.jacobandreas.steph.strategy;

import net.jacobandreas.steph.tournament.*;
import java.util.*;

/**
 * Strategy to pair round 2 of a tournament.
 *
 * @author jacob
 */
public class Round2PairingStrategy extends BasePairingStrategy {

    private final boolean higherNumberGoesFirst;

    /**
     * Creates a new Round2PairingStrategy with the given rule about rank
     * selection for ties
     * @param higherNumberGoesFirst
     */
    public Round2PairingStrategy(boolean higherNumberGoesFirst) {
        this.higherNumberGoesFirst = higherNumberGoesFirst;
    }

    /**
     * Pairs the round, using only record and PD to determine swap "closeness".
     * @param teams the teams to pair
     * @return a paired Round
     */
    public Round pair(ArrayList<Team> teams) {

        Round round = new Round();

        ArrayList<Team> needsPi = new ArrayList<Team>(teams.size() / 2);
        ArrayList<Team> needsDelta = new ArrayList<Team>(teams.size() / 2);

        for(Team t : teams) {
            if(t.getLastSide() == Match.PI) {
                needsDelta.add(t);
            } else if(t.getLastSide() == Match.DELTA) {
                needsPi.add(t);
            } else {
                assert false : "Sanity check failed: team not assigned a side last round";
            }
        }
        // sort the two stacks
        Collections.sort(needsPi, getRound2Comparator());
        Collections.sort(needsDelta, getRound2Comparator());

        // resolve impermissibles
        sideConstrainedResolveImpermissibles(needsPi, needsDelta);

        // double-check our pairings and add them to the round
        for(int i = 0; i < needsPi.size(); i++) {
            Team team1 = needsPi.get(i);
            Team team2 = needsDelta.get(i);
            assert isPermitted(team1, team2) :
               "Sanity check failed: impermissibles paired (2)";
            Match m = new Match(team1, team2);
            round.addMatch(m);
        }

        return round;

    }

    /**
     * Get a comparator to determine swap desirability. The most desirable swaps
     * have a minimal difference in wins, point differential and rank
     * @return a comparator to order swaps
     */
    public Comparator<Swap> swapComparator() {
        return new Comparator<Swap>() {
            public int compare(Swap s1, Swap s2) {
                double winDD = s1.getWinDifference() - s2.getWinDifference();
                //i.e. Win-Difference-Difference
                if(Math.abs(winDD) > TOLERANCE) {
                    return (int) Math.signum(winDD);
                }
                double pdDD = s1.getPDDifference() - s2.getPDDifference();
                //i.e. Point-Differential-Difference-Difference
                if(Math.abs(pdDD) > TOLERANCE) {
                    return (int) Math.signum(pdDD);
                }
                int rankSumD = s1.getRankSum() - s2.getRankSum();
                if(Math.abs(rankSumD) > TOLERANCE) { // but these are ints
                    return (int) -Math.signum(rankSumD);
                    // higher rank sums come "first"
                }
                if(s1.getSide() == Match.DELTA && s2.getSide() == Match.PI) {
                    return -1;
                } else if(s1.getSide() == Match.PI && s2.getSide() == Match.DELTA) {
                    return 1;
                } else {
                    System.err.println("Sanity check failed: unable to rank swaps!");
                    return 0;
                }
            }
        };
    }

    /**
     * Gets a comparator to determine round rankings. We order teams by their
     * win record, then by their point differential (skipping CS).
     * @return a comparator to order teams in the card stack
     */
    public Comparator<Team> getRound2Comparator() {
        return new Comparator<Team>() {

            public int compare(Team t1, Team t2) {
                double winD = t2.getWins() - t1.getWins();
                if(Math.abs(winD) > TOLERANCE) {
                    return (int) Math.signum(winD);
                }
                double pD = t2.getPointDifferential() - t1.getPointDifferential();
                if(Math.abs(pD) > TOLERANCE) {
                    return (int) Math.signum(pD);
                }
                if(higherNumberGoesFirst) {
                    return t2.getNumber() - t1.getNumber();
                } else {
                    return t1.getNumber() - t2.getNumber();
                }
            }

        };
    }


}
