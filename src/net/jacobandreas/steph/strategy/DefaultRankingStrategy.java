package net.jacobandreas.steph.strategy;

import java.util.*;
import net.jacobandreas.steph.tournament.*;

/// All signs for these comparators are the opposite of what we we would expect,
/// (i.e. high numbers come before low numbers) because the highest values
/// produce the lowest ranks (e.g. the team with the most wins gets the lowest
/// rank (1st).

/**
 * Standard AMTA strategy to determine final rankings for a tournament.
 *
 * @author jacob
 */
public class DefaultRankingStrategy implements RankingStrategy {
    
    private CoinFlipStrategy coinflips;

    // to avoid roundoff errors in float arithmetic
    public static final double TOLERANCE = 0.01;

    /**
     * Creates a new DefaultRankingStrategy which will rely on the given
     * CoinFlipStrategy for flips.
     * @param s the strategy to use for coin flips
     */
    public DefaultRankingStrategy(CoinFlipStrategy s) {
        coinflips = s;
    }

    /**
     * Generates an ordered list of teams, with the first place winner at index
     * 0. Does not modify the original collection of teams.
     * @param teams The teams to rank
     * @return An ordered list of teams
     */
    public List<Team> rank(Collection<Team> teams) {
        List<Team> myteams = new ArrayList<Team>();
        myteams.addAll(teams);
        // Rank teams using only record and CS (needed to figure out tied groups
        // for SOL and SOW calculation)
        Collections.sort(myteams, getRecordAndCSComparator());
        markSOWRank(myteams);
        markSOLRank(myteams);
        // Produce final ranking using marked SOW and SOL
        Collections.sort(myteams, getRankingComparator());
        return myteams;
    }

    /**
     * Gets a comparator that sorts teams only according to their Win/Loss
     * record and Combined Strength.
     * @return a list of teams ranked by record, then by CS
     */
    private Comparator<Team> getRecordAndCSComparator() {
        return new Comparator<Team>() {
            public int compare(Team a, Team b) {

                // record

                if(Math.abs(a.getWins() - b.getWins()) > TOLERANCE) {
                    return (int) -Math.signum(a.getWins() - b.getWins());
                }

                // cs

                if(a.getCombinedStrength() != b.getCombinedStrength()) {
                    return (int) -Math.signum(a.getCombinedStrength() - b.getCombinedStrength());
                }

                return 0;
            }
        };
    }

    /**
     * Gets a comparator to determine final rankings, using (in order):
     * Win/Loss record, Combined Strength, Strength of Opposition, Point
     * Differential, Total Points Earned and Random Coin Toss.
     * @return
     */
    private Comparator<Team> getRankingComparator() {
        return new Comparator<Team>() {
            public int compare(Team a, Team b) {

                // record and cs (just reuse the old comparator)

                Comparator<Team> c1 = getRecordAndCSComparator();

                if(c1.compare(a, b) != 0) {
                    return c1.compare(a, b);
                }

                // soo

                double sooDiff = a.getSOORank() - b.getSOORank();
                if(Math.abs(sooDiff) > TOLERANCE) {
                    return (int) -Math.signum(sooDiff);
                }

                // pd, dropping high/low ballots if there is a tie

                for(int drop = 0; drop <= 3; drop++) {
                    int pdDiff = -(a.getPointDifferentialDropping(drop) - b.getPointDifferentialDropping(drop));
                    if(pdDiff != 0) {
                        return pdDiff;
                    }
                }

                // raw points, dropping high/low ballots if there is a tie

                for(int drop = 0; drop <= 3; drop++) {
                    int pointDiff = -(a.getPointsDropping(drop) - b.getPointsDropping(drop));
                    if(pointDiff != 0) {
                        return pointDiff;
                    }
                }

                // coin toss

                switch(coinflips.getFlip("Coin flip to determine final ranking:",
                        a + " has higher rank", b + " has higher rank")) {
                    case CoinFlipStrategy.HEADS: return -1;
                    default: return 1;
                }
            }
        };
    }

    /**
     * Marks teams with their Strength of Win Rank. For an explanation of how
     * to calculate SOW (and an explanation of the differences between SOW and
     * SOW Rank) please consult the Tab Manual.
     * @param teams the teams to mark
     */
    private void markSOWRank(List<Team> teams) {
        // the rank that should be assigned to the next unranked team
        int leadRank = 1;
        // holds all the teams tied with a given sow
        LinkedList<Team> queue = new LinkedList<Team>();
        for(Team curr : teams) {
            if(queue.isEmpty()) {
                // we haven't seen anybody with this SOW yet
                queue.addLast(curr);
                continue;
            }
            // check to see if we're tied on record/cs and SOW with the other
            // teams in the queue (i.e., if we should have the same SOW Rank)
            Team prev = queue.peekLast();
            boolean recordCSTie = Math.abs(curr.getWins() - prev.getWins()) < TOLERANCE &&
                    Math.abs(curr.getCombinedStrength() - prev.getCombinedStrength()) < TOLERANCE;
            boolean sowTie = Math.abs(curr.getStrengthOfWin() - prev.getStrengthOfWin()) < TOLERANCE;
            if(!(recordCSTie && sowTie)) {
                // if not (i.e. we're no longer looking at a tie), go back and
                // assign the shared rank of all the teams in the queue (which
                // are tied).
                int tailRank = leadRank + queue.size() - 1;
                double avgRank = (leadRank + tailRank) / 2d;
                for(Team tiedTeam : queue) {
                    tiedTeam.setSOWRank(avgRank);
                }
                // Then, reset the queue with the current element
                queue = new LinkedList<Team>();
                if(recordCSTie) {
                    // the current team's SOW will be compared to the teams that were
                    // in the queue, so make its rank greater
                    leadRank += tailRank;
                } else {
                    // the current team's SOW will not be compared to the teams that
                    // were in the queue, so reset the rank counter
                    leadRank = 1;
                }
            }
            queue.addLast(curr);
        }
    }

    /**
     * Marks teams with their Strength of Win Rank. For an explanation of how
     * to calculate SOW (and an explanation of the differences between SOW and
     * SOW Rank) please consult the Tab Manual.
     *
     * //TODO This should probably be consolidated with markSOWRank
     * 
     * @param teams the teams to mark
     */
    private void markSOLRank(List<Team> teams) {
        // the rank that should be assigned to the next unranked team
        int leadRank = 1;
        // holds all the teams tied with a given sol
        LinkedList<Team> queue = new LinkedList<Team>();
        for(Team curr : teams) {
            if(queue.isEmpty()) {
                // we haven't seen anybody with this SOL yet
                queue.addLast(curr);
                continue;
            }
            // check to see if we're tied on record/cs and SOL with the other
            // teams in the queue (i.e., if we should have the same SOL Rank)
            Team prev = queue.peekLast();
            boolean recordCSTie = Math.abs(curr.getWins() - prev.getWins()) < TOLERANCE &&
                    Math.abs(curr.getCombinedStrength() - prev.getCombinedStrength()) < TOLERANCE;
            boolean solTie = Math.abs(curr.getStrengthOfLoss() - prev.getStrengthOfLoss()) < TOLERANCE;
            if(!(recordCSTie && solTie)) {
                // if not (i.e. we're no longer looking at a tie), go back and
                // assign the shared rank of all the teams in the queue (which
                // are tied).
                int tailRank = leadRank + queue.size() - 1;
                double avgRank = (leadRank + tailRank) / 2d;
                for(Team tiedTeam : queue) {
                    tiedTeam.setSOLRank(avgRank);
                }
                // Then, reset the queue with the current Team
                queue = new LinkedList<Team>();
                if(recordCSTie) {
                    // the current team's SOW will be compared to the teams that were
                    // in the queue, so make its rank greater
                    leadRank += tailRank;
                } else {
                    // the current team's SOW will not be compared to the teams that
                    // were in the queue, so reset the rank counter
                    leadRank = 1;
                }
            }
            queue.addLast(curr);
        }
    }

}
