package net.jacobandreas.steph.event;

/**
 * The listener interface for receiving pairing events. Allows a simulator to
 * listen for pairing events of interest (e.g. long pairer's lists or large
 * reaches), or a pairing helper to identify pairing situations requiring
 * intervention from a tournament director.
 *
 * @author jacob
 */
public interface PairingEventListener {

    /**
     * String description of an unpairable round.
     */
    public static final String UNPAIRABLE = "Unpairable round!";

    /**
     * Invoked when a pairing event occurs.
     * @param event A description of the event that occurred
     * @param data An object containing additional data about the pairing event
     */
    public void pairingEventOccurred(String event, Object data);

}
