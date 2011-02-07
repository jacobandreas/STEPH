package net.jacobandreas.steph.tournament;

/**
 * A single (summarized) ballot in a tournament, recording only the final
 * score for each team. Immutable.
 * @author jacob
 */
public class Ballot {

    private final Team piTeam, deltaTeam;
    private final int piTotal, deltaTotal;

    /**
     * Creates a new ballot for the given teams, with the given scores.
     * @param piTeam
     * @param deltaTeam
     * @param piTotal
     * @param deltaTotal
     */
    public Ballot(Team piTeam, Team deltaTeam, Integer piTotal, Integer deltaTotal) {
        this.piTeam = piTeam;
        this.deltaTeam = deltaTeam;
        this.piTotal = piTotal;
        this.deltaTotal = deltaTotal;
    }

    public int getPiTotal() {
        return piTotal;
    }

    public int getDeltaTotal() {
        return deltaTotal;
    }

    public int getPiPD() {
        return getPiTotal() - getDeltaTotal();
    }

    public int getDeltaPD() {
        return getDeltaTotal() - getPiTotal();
    }

    public Team getPi() {
        return piTeam;
    }

    public Team getDelta() {
        return deltaTeam;
    }

    /**
     * Gets the point differential from this ballot for the given team
     * @param t the team whose PD we're retreiving
     * @return t's pd from this ballot
     */
    public int getPDFor(Team t) {
        if(t.equals(piTeam)) {
            return getPiPD();
        } else if(t.equals(deltaTeam)) {
            return getDeltaPD();
        } else {
            throw new IllegalArgumentException(this + " does not belong to " + t);
        }
    }

    /**
     * Gets the opponent for the given team from this ballot
     * @param t the team whose opponent we're retrieving
     * @return t's oppnent on this ballot
     */
    public Team getOpponentFor(Team t) {
        if(t.equals(piTeam)) {
            return getDelta();
        } else if(t.equals(deltaTeam)) {
            return getPi();
        } else {
            throw new IllegalArgumentException(this + " does not belong to " + t);
        }
    }

    /**
     * Gets the point total for the given team from this ballot
     * @param t the team whose point total we're retrieving
     * @return t's point total
     */
    public int getTotalFor(Team t) {
        if(t.equals(piTeam)) {
            return getPiTotal();
        } else if(t.equals(deltaTeam)) {
            return getDeltaTotal();
        } else {
            throw new IllegalArgumentException(this + " does not belong to " + t);
        }
    }

    /**
     * Gets the side the given team was on in this ballot
     * @param t the team whose side we're looking up
     * @return t's side on this ballot
     */
    public int getSideFor(Team t) {
        if(t.equals(piTeam)) {
            return Match.PI;
        } else if(t.equals(deltaTeam)) {
            return Match.DELTA;
        } else {
            throw new IllegalArgumentException(this + " does not belong to " + t);
        }
    }

    /**
     * Gets a string representation of this ballot
     * @return a string representation of this ballot
     */
    @Override
    public String toString() {
        return "Ballot: " + piTeam + " (" + piTotal + "), " + deltaTeam + " (" + deltaTotal + ")";
    }

}
