package net.jacobandreas.steph.tournament;

/**
 * Represents a swap between two teams, for resolving impermissibles.
 * @author jacob
 */
public class Swap {

    private Team team1, team2;
    private int side;

    /**
     * Creates a new swap of the two teams
     * @param t1 the first team in the swap
     * @param t2 the sescond team in the swap
     * @param side the side on which the swap is occurring
     */
    public Swap(Team t1, Team t2, int side) {
        if(t1.getNumber() < t2.getNumber()) {
            team1 = t1;
            team2 = t2;
        } else {
            team1 = t2;
            team2 = t1;
        }
        this.side = side;
    }
    
    public Swap(Team t1, Team t2) {
        this(t1, t2, Match.NO_SIDE);
    }

    public Team getTeam1() {
        return team1;
    }
    
    public Team getTeam2() {
        return team2;
    }

    public int getSide() {
        return side;
    }

    public double getWinDifference() {
        return Math.abs(getTeam1().getWins() - getTeam2().getWins());
    }

    public double getCSDifference() {
        return Math.abs(getTeam1().getCombinedStrength() - getTeam2().getCombinedStrength());
    }

    public double getPDDifference() {
        return Math.abs(getTeam1().getPointDifferential() - getTeam2().getPointDifferential());
    }

    public int getRankSum() {
        return getTeam1().getRank() + getTeam2().getRank();
    }

    public boolean contains(Team t) {
        return getTeam1().equals(t) || getTeam2().equals(t);
    }

    @Override
    public String toString() {
        return getTeam1() + " with " + getTeam2();
    }
    
    @Override
    public boolean equals(Object other) {
        if(other.getClass() != Swap.class) {
            return false;
        }
        Swap oSwap = (Swap)other;
        return (oSwap.getTeam1().equals(getTeam1()) && oSwap.getTeam2().equals(getTeam2()))
                || (oSwap.getTeam1().equals(getTeam2()) && oSwap.getTeam2().equals(getTeam1()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.team1 != null ? this.team1.hashCode() : 0);
        hash = 41 * hash + (this.team2 != null ? this.team2.hashCode() : 0);
        return hash;
    }

}
