
package app.jbomberman.util;

import java.util.Random;


/**
 * RandomNumberGenerator is a wrapper for a java.util.Random object that is kept as
 * a private static field using the singleton pattern. Its aim is to create a random
 * number generator and to use it uniformly from different classes. We need a lot of randomness
 * in the application and creating many random number generators is useless and time-consuming
 *
 * @author gp
 */
public class RandomNumberGenerator
{
    /**
     * rng is the unique random number generator
     */
    private static Random rng;

    /**
     * @return the unique instance of java.util.random
     */
    public static Random getInstance()
    {
        if (rng == null)
            rng = new Random();
        return rng;
    }
}
