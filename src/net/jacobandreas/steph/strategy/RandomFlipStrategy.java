package net.jacobandreas.steph.strategy;

/**
 * Determines the outcome of a coin flip randomly (useful for simulations).
 *
 * @author jacob
 */
public class RandomFlipStrategy implements CoinFlipStrategy {

    /**
     * Gets the outcome of this flip
     * @param message (not used)
     * @param headsm (not used)
     * @param tailsm (not used)
     * @return the outcome of the flip.
     */
    public int getFlip(String message, String headsm, String tailsm) {
        int flip = (int)(Math.random() * 2);
        if(flip == 0) {
            return HEADS;
        }
        return TAILS;
    }

}
