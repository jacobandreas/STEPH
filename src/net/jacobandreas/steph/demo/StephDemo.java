package net.jacobandreas.steph.demo;

import net.jacobandreas.steph.exception.*;
import net.jacobandreas.steph.strategy.*;
import net.jacobandreas.steph.tournament.*;

/**
 * Provides a main method, giving a basic example of how to use STEPH.
 * @author jacob
 */
public class StephDemo {

    /**
     * The number of teams that will compete in this tournament
     */
    public static final int NUM_TEAMS = 20;

    /**
     * The higher-numbered team should get a better rank while pairing
     */
    public static final boolean HIGHER_FIRST = true;
    /**
     * The left column should go pros/plaintiff in round 3
     */
    public static final boolean PI_LEFT = true;

    /**
     * Pair this tournament using the set of strategies that would be used
     * at nationals
     */
    public static final PairingStrategy[] STRATEGIES = {
        new Round1PairingStrategy(),
        new Round2PairingStrategy(HIGHER_FIRST),
        new Round3PairingStrategy(HIGHER_FIRST, PI_LEFT),
        new Round4NationalsPairingStrategy(HIGHER_FIRST)
    };

    /**
     * Choose coin flips randomly
     */
    public static final CoinFlipStrategy FLIPPER = new RandomFlipStrategy();
    /**
     * Use the standard ranker for teams
     */
    public static final RankingStrategy RANKER = new DefaultRankingStrategy(FLIPPER);


    /**
     * Runs a simulated tournament, creating a random field of teams, adding
     * random ballots, and then printing the final rankings to stdout.
     */
    public static void main(String[] args) {
        
        try {

            // create a new tournament object
            Tournament tournament = new Tournament();

            // fill the tournament with teams
            setupTeams(tournament);

            // play each round of the tournament
            for(int i = 0; i < STRATEGIES.length; i++) {
                playRound(tournament, i);
            }

            // print the final rankings
            System.out.println(
                Tournament.prettyPrintTeams(RANKER.rank(tournament.getTeams()))
            );

            
        } catch(TournamentStateException e) {
            System.err.println("An illegal tournament state occurred. Please " +
                    "check your team and ballot assisgnments for mistakes.\n" +
                    "Details:\n");
            e.printStackTrace();
        }

    }

    /**
     * Fills this tournament with teams, numbered starting from 1
     * @param tournament The tournament to fill
     */
    public static void setupTeams(Tournament tournament) {
        for(int i = 0; i < NUM_TEAMS; i++) {
            tournament.addTeam(new Team("Team number " + i, i));
        }
    }

    /**
     * For a single round of this tournament, pairs, adds ballots and then plays.
     * @param tournament the tournament to use
     * @param round the number of the round we're adding
     */
    public static final void playRound(Tournament tournament, int round) throws TournamentStateException {
        // pair a new round using the appropriate strategy
        tournament.addRound(STRATEGIES[round]);
        // lock the pairings
        tournament.getCurrentRound().setPairingLocked(true);
        // add ballots
        addRandomBallots(tournament);
        // commit ballots
        tournament.getCurrentRound().play();
    }

    /**
     * Adds two random ballots to each match in the lastest round of this
     * tournament. Scores are in the range 100-139, to give a realistic
     * distribution.
     * @param tournament the tournament to add ballots to
     */
    public static void addRandomBallots(Tournament tournament) {
        for(Match match : tournament.getCurrentRound().getMatches()) {
            // pick two random scores
            int piTotal1 = 100 + (int)(Math.random() * 40);
            int deltaTotal1 = 100 + (int)(Math.random() * 40);

            // add a new ballot with those scores
            match.addBallot(new Ballot(match.getPi(), match.getDelta(),
                    piTotal1, deltaTotal1));

            // et cetera
            int piTotal2 = 200 + (int)(Math.random() * 40);
            int deltaTotal2 = 200 + (int)(Math.random() * 40);

            match.addBallot(new Ballot(match.getPi(), match.getDelta(),
                    piTotal2, deltaTotal2));

        }
    }

}
