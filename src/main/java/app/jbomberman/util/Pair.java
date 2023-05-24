package app.jbomberman.util;

/**
 * Pair represents a pair of objects
 *
 * @param <T> the type of the first object
 * @param <S> the type of the second object
 * @author gp
 */
public class Pair<T, S>
{
    /**
     * x is the first element of the pair
     */
    private T x;
    /**
     * y is the second element of the pair
     */
    private S y;

    /**
     * Create a Pair from two values
     *
     * @param x the first value
     * @param y the second value
     */
    public Pair(T x, S y)
    {
        this.x = x;
        this.y = y;
    }


    /**
     * get first value
     *
     * @return the first value
     */
    public T getX()
    {
        return x;
    }

    /**
     * get second value
     *
     * @return the second value
     */
    public S getY()
    {
        return y;
    }

    /**
     * We must provide some way to allow subclasses to set the x attribute, so
     * it's protected.
     *
     * @param x set the first value
     */
    protected void setX(T x)
    {
        this.x = x;
    }

    /**
     * We must provide some way to allow subclasses to set the y attribute, so
     * it's protected.
     *
     * @param y set the second value
     */
    protected void setY(S y)
    {
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "Pair(x=" + x + ", y=" + y + ")";
    }
}
