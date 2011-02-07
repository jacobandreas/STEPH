package net.jacobandreas.steph.strategy;

/**
 * Interface implemented by coin flip strategies, used to determine the outcome
 * of various random draws required by the pairing process.
 * @author jacob
 */
public interface CoinFlipStrategy {

    public static final int HEADS = 1;
    public static final int TAILS = -1;

    /**
     * Gets the result of the coin flip
     * @param message A description of the purpose of this coin flip
     * @param headsm A description of the consequences of an outcome of "heads"
     * @param tailsm A description of the consequences of an outcome of "tails"
     * @return the side of the coin that came up
     */
    public int getFlip(String message, String headsm, String tailsm);

}