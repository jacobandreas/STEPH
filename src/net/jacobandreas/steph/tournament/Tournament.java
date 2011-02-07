package net.jacobandreas.steph.tournament;

import net.jacobandreas.steph.event.*;
import net.jacobandreas.steph.strategy.*;
import net.jacobandreas.steph.exception.*;
import java.util.*;

/**
 * Represents a complete mock trial tournament.
 * @author jacob
 */
public class Tournament implements PairingEventListener {

    public static final int MAX_TEAMS = 48;
    public static final int ROUNDS = 4;

    private ArrayList<Team> teams;
    private ArrayList<Round> rounds;

    /**
     * Creates a new tournament.
     */
    public Tournament() {
        teams = new ArrayList<Team>();
        rounds = new ArrayList<Round>();
    }

    /**
     * Gets the number of rounds that have been paired so far. (NOT the number
     * of rounds already played; NOT the maximum number of rounds allowed.)
     * @return the number of rounds already paired
     */
    public int getNumRounds() {
        return rounds.size();
    }

    /**
     * Gets the Round object for the given round number.
     * @param round the number of the Round to retrieve
     * @return the appropriate Round
     */
    public Round getRound(int round) {
        return rounds.get(round);
    }

    /**
     * Gets the Round object for the most recently created round in the tournament.
     * @return the current round
     */
    public Round getCurrentRound() {
        return rounds.get(rounds.size() - 1);
    }

    /**
     * Adds a team to this tournament
     * @param t the team to add
     */
    public void addTeam(Team t) {
        teams.add(t);
    }

    /**
     * Creates a new round in this tournament, pairing it using the given
     * strategy.
     * @param strategy The strategy to use in pairing the round
     * @throws net.jacobandreas.steph.exception.TournamentStateException
     */
    public void addRound(PairingStrategy strategy) throws TournamentStateException {
        rounds.add(strategy.pair(teams));
    }

    /**
     * Throws away the most recently created round.
     * @throws net.jacobandreas.steph.exception.TournamentStateException if
     * the round was already locked
     */
    public void discardLastRound() throws TournamentStateException {
        if(getCurrentRound().isPairingLocked()) {
            throw new TournamentStateException("Attempting to discard a locked round");
        }
        rounds.remove(getCurrentRound());
    }
    
    public void pairingEventOccurred(String event, Object data) {
    }

    /**
     * Generates a nice human-readable representation of a list of teams.
     * @param teams the teams to print
     * @return a String representation of the teams.
     */
    public static String prettyPrintTeams(List<Team> teams) {
        StringBuilder s = new StringBuilder();

        for(Team t : teams) {
            s.append(t.getNumber() + " (" + t.getName() + ")\n");
            s.append("   wins: " + t.getWins() + "\n");
            s.append("   cs: " + t.getCombinedStrength() + "\n");
            s.append("   pd: " + t.getPointDifferential() + "\n");
            s.append("   points: " + t.getPoints() + "\n");
            s.append("\n");
        }

        return s.toString();
    }

    /**
     * Gets a list of all the teams in this tournament.
     * @return this tournament's teams
     */
    public Collection<Team> getTeams() {
        return Collections.unmodifiableCollection(teams);
    }

}
