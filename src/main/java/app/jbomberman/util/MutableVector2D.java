
package app.jbomberman.util;

/**
 * MutableVector2D represents a mutable 2D coordinate object
 *
 * @author gp
 */
public class MutableVector2D extends Vector2D
{
    /**
     * Construct a MutableVector2D object from (x, y) coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public MutableVector2D(int x, int y)
    {
        super(x, y);
    }

    public MutableVector2D(Vector2D vector2D)
    {
        super(vector2D.getX(),
            vector2D.getY());
    }

    /**
     * getValuesFrom sets the values of x and y of this getting them from the
     * ones of another coordinate object
     *
     * @param c the coordinate object whose values must be used to initialize
     *          this.x and this.y
     */
    public void getValuesFrom(Vector2D c)
    {
        setX(c.getX());
        setY(c.getY());
    }

    /**
     * @param x set the x attribute
     */
    public void setX(int x)
    {
        super.setX(x);
    }

    /**
     * @param y set the y attribute
     */
    public void setY(int y)
    {
        super.setY(y);
    }

    @Override
    public String toString()
    {
        return "MutableVector2D(x=" + getX() + ", y=" + getY() + ")";
    }
}
