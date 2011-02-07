package net.jacobandreas.steph.exception;

/**
 * Thrown to indicate that the user has attempted to put the tournament into
 * an illegal state. Note that this exception is explicitly not for dealing with
 * internal program errors (which should generate an AssertionError) or
 * unpairable rounds (which should generate a PairingEvent).
 *
 * @author jacob
 */
public class TournamentStateException extends Exception {

    /**
     * Constructs a TournamentStateException with the given description.
     * @param desc the description
     */
    public TournamentStateException(String desc) {
        super(desc);
    }

}
