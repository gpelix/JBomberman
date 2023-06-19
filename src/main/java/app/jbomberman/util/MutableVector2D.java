package app.jbomberman.util;

/**
 * MutableVector2D represents a mutable 2D vector object. It's much like a
 * Vector2D object, but it's mutable, and we don't use the FlightWeight pattern
 * anymore (it really cannot be used unless we did something like a copy
 * on write on Vector2D)
 *
 * @author gp
 */
public class MutableVector2D extends Vector2D
{
	/**
	 * Construct a MutableVector2D object from a Vector2D
	 *
	 * @param vector2D the 2D vector whose coordinates must be taken and set
	 *				 for the new MutableVector2D
	 */
	public MutableVector2D(Vector2D vector2D)
	{
		super(vector2D.getX(),
			vector2D.getY());
	}

	/**
	 * getValuesFrom sets the values of x and y of this getting them from the
	 * ones of another coordinate object
	 *
	 * @param c the vector2D object whose values must be used to initialize
	 *		  this.x and this.y
	 */
	public void getValuesFrom(Vector2D c)
	{
		setX(c.getX());
		setY(c.getY());
	}

	/**
	 * set the x attribute
	 *
	 * @param x the x attribute to set
	 */
	public void setX(Integer x)
	{
		super.setX(x);
	}

	/**
	 * set the y attribute
	 *
	 * @param y the y attribute to set
	 */
	public void setY(Integer y)
	{
		super.setY(y);
	}

	@Override
	public String toString()
	{
		return "MutableVector2D(x=" + getX() + ", y=" + getY() + ")";
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
