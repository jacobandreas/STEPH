package net.jacobandreas.steph.strategy;

import net.jacobandreas.steph.tournament.*;
import java.util.*;

/**
 * Strategy to pair around like in the fourth round of Gold Nationals (i.e.
 * no brackets, no protection).
 *
 * @author jacob
 */
public class Round4NationalsPairingStrategy extends BasePairingStrategy {

    private boolean higherNumberGoesFirst;

    /**
     * Creates a new Round4NationalsPairingStrategy with the given preference
     * about rank numbering.
     * @param higherNumberGoesFirst
     */
    public Round4NationalsPairingStrategy(boolean higherNumberGoesFirst) {
        this.higherNumberGoesFirst = higherNumberGoesFirst;
    }

    /**
     * Pairs the round side-constrained, ordering and breaking ties using
     * record, then CS, then PD (this is identical to the round 2 pairing
     * except for the use of PD).
     * @param teams the teams to pair
     * @return The paired Round
     */
    public Round pair(ArrayList<Team> teams) {

        Round round = new Round();

        // make our stacks
        ArrayList<Team> needsPi = new ArrayList<Team>(teams.size() / 2);
        ArrayList<Team> needsDelta = new ArrayList<Team>(teams.size() / 2);

        // add each team to the right stack
        for(Team t : teams) {
            if(t.getLastSide() == Match.PI) {
                needsDelta.add(t);
            } else if(t.getLastSide() == Match.DELTA) {
                needsPi.add(t);
            } else {
                assert false : "Sanity check failed: team not assigned a side last round";
            }
        }

        // sort the stacks
        Collections.sort(needsPi, getRound4Comparator());
        Collections.sort(needsDelta, getRound4Comparator());

        // resolve impermissibles
        List<Swap> swaps = sideConstrainedResolveImpermissibles(needsPi, needsDelta);
        if(swaps.size() >= 25) {
            reportPairingEvent("25 swaps", swaps);
        }

        // double check our pairings and add them to the round
        for(int i = 0; i < needsPi.size(); i++) {
            Team team1 = needsPi.get(i);
            Team team2 = needsDelta.get(i);
            assert isPermitted(team1, team2) :
               "Sanity check failed: impermissibles paired (4)";

            Match m = new Match(team1, team2);
            round.addMatch(m);
        }

        return round;

    }

    /**
     * Gets a comparator to rank candidate swaps by closeness.
     * @return the swap comparator
     */
    public Comparator<Swap> swapComparator() {
        return new Comparator<Swap>() {
            public int compare(Swap s1, Swap s2) {
                double winDD = s1.getWinDifference() - s2.getWinDifference();
                //i.e. Win-Difference-Difference
                if(Math.abs(winDD) > TOLERANCE) {
                    return (int) Math.signum(winDD);
                }
                double csDD = s1.getCSDifference() - s2.getCSDifference();
                if(Math.abs(csDD) > TOLERANCE) {
                    return (int) Math.signum(csDD);
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
     * Gets a comparator to rank teams for their position in the card stack, using
     * record, then cs, then pd.
     * @return a team comparator
     */
    public Comparator<Team> getRound4Comparator() {
        return new Comparator<Team>() {

            public int compare(Team t1, Team t2) {
                double winD = t2.getWins() - t1.getWins();
                if(Math.abs(winD) > TOLERANCE) {
                    return (int) Math.signum(winD);
                }
                double csD = t2.getCombinedStrength() - t1.getCombinedStrength();
                if(Math.abs(csD) > TOLERANCE) {
                    return (int) Math.signum(csD);
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
