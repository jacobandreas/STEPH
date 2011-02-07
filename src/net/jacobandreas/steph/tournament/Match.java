package net.jacobandreas.steph.tournament;

import java.util.*;

/**
 * A single match between two teams in a tournament round.
 *
 * @author jacob
 */
public class Match {

    /**
     * Represents Prosecution/Plaintiff.
     */
    public static final int PI = 1;
    /**
     * Represents Defense.
     */
    public static final int DELTA = 2;
    /**
     * Indicates that party is unimportant.
     */
    public static final int NO_SIDE = 3;

    /**
     * The most ballots that can be assigned in a given round.
     */
    public static final int MAX_BALLOTS = 2;

    private boolean played;

    private Team pi;
    private Team delta;

    private ArrayList<Ballot> ballots;

    /**
     * Creates a new match with the given teams.
     * @param _pi
     * @param _delta
     */
    public Match(Team _pi, Team _delta) {
        pi = _pi;
        delta = _delta;

        ballots = new ArrayList<Ballot>();

        played = false;
    }

    /**
     * Tests to see if this match equals another object
     * @param o the object we're comparing to
     * @return true if o is a match with the same pi and delta teams, false
     * otherwise
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(o.getClass() != getClass()) {
            return false;
        }
        Match m = (Match) o;
        return m.getPi() == getPi() && m.getDelta() == getDelta();
    }

    /**
     * Gets a numerical representation of this match
     * @return an integer hash of this match
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.pi != null ? this.pi.hashCode() : 0);
        hash = 29 * hash + (this.delta != null ? this.delta.hashCode() : 0);
        return hash;
    }

    /**
     * Adds a ballot to this match
     * @param b the ballot to add
     */
    public void addBallot(Ballot b) {
        ballots.add(b);
    }

    /**
     * Removes a ballot from this match
     * @param b the ballot to remove
     */
    public void removeBallot(Ballot b) {
        ballots.remove(b);
    }

    /**
     * Gets an indexed ballot from this match
     * @param i the index of the ballot to retreive
     * @return the ith ballot
     */
    public Ballot getBallot(int i) {
        return ballots.get(i);
    }

    /**
     * Gets the number of ballots assigned in this match
     * @return the number of ballots currently added
     */
    public int getNumBallots() {
        return ballots.size();
    }

    /**
     * Gets the index of the given ballot in the round
     * @param b the ballot to look up
     * @return b's index
     */
    public int indexOfBallot(Ballot b) {
        return ballots.indexOf(b);
    }

    /**
     * Tests whether this match involved the given team
     * @param t the team to look for
     * @return true if t participated in this match, false otherwise
     */
    public boolean contains(Team t) {
        return t.equals(getPi()) || t.equals(getDelta());
    }

    /**
     * Add each of this match's ballots to its teams' histories, and set
     * played to true.
     */
    public void play() {

        for(Ballot ballot : ballots) {
            pi.addBallot(ballot);
            delta.addBallot(ballot);
        }

        played = true;

    }

    public boolean isPlayed() {
        return played;
    }

    @Override
    public String toString() {
        return "P " + pi + " vs D " + delta + " :: " + ballots;
    }

    public Team getPi() {
        return pi;
    }

    public Team getDelta() {
        return delta;
    }

}
