package app.jbomberman.util;

/**
 * CallbackOnce is a special type of Callback that has the runOnce method that
 * even though it gets called many times, it will only execute once.
 *
 * @author gp
 * @implNote Even though it's a SAM, it cannot be made interface as we need the
 * hasRun field. This is a bit nasty as we cannot declare CallbackOnce's objects
 * on the fly with a method reference or a lambda
 */
public abstract class CallbackOnce implements Callback
{
    /**
     * hasRun is a boolean that is true if and only if the runOnce method
     * has been run once
     */
    private boolean hasRun = false;

    /**
     * runOnce calls run if and only if it has not called before
     */
    public void runOnce()
    {
        if (!hasRun) {
            run();
            hasRun = true;
        }
    }

    public void reset()
    {
        hasRun = false;
    }
}
