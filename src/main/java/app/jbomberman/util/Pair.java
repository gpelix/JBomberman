
package app.jbomberman.util;

public class Pair<T, S>
{
    /**
     * x coordinate
     */
    private T x;
    /**
     * y coordinate
     */
    private S y;

    /**
     * @param x x coordinate
     * @param y y coordinate
     */
    public Pair(T x, S y)
    {
        this.x = x;
        this.y = y;
    }


    /**
     * of the x coordinate
     *
     * @return the x coordinate
     */
    public T getX() { return x; }

    /**
     * of the y coordinate
     *
     * @return the y coordinate
     */
    public S getY() { return y; }

    /**
     * We must provide some way to allow subclasses to set the x attribute, so
     * it's protected. PLEASE DON'T CALL ON A COORDINATE WHICH IS NOT ALSO AN
     * INSTANCE OF A MUTABLE SUBCLASS OF COORDINATE. Thanks to this,
     * subclasses might be made mutable
     *
     * @param x set the x attribute
     */
    protected void setX(T x) { this.x = x; }

    /**
     * We must provide some way to allow subclasses to set the y attribute, so
     * it's protected. PLEASE DON'T CALL ON A COORDINATE WHICH IS NOT ALSO AN
     * INSTANCE OF A MUTABLE SUBCLASS OF COORDINATE. Thanks to this,
     * subclasses might be made mutable
     *
     * @param y set the y attribute
     */
    protected void setY(S y) { this.y = y; }

    @Override
    public String toString()
    {
        return "Pair(x=" + x + ", y=" + y + ")";
    }
}
