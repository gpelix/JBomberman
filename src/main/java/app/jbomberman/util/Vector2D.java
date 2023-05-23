
package app.jbomberman.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

public class Vector2D extends Pair<Integer, Integer>
{
	/**
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	protected Vector2D(int x, int y)
	{
		super(x, y);
	}

	/**
	 * vector sum of coordinates
	 *
	 * @param coordinate the other coordinate that must be summed to this
	 * @return a new coordinate that has as x attribute the sum of this.x and
	 * coordinate.getX() and the same thing happens for the y attribute
	 */
	public Vector2D sum(Vector2D other)
	{
		return Vector2D.of(getX() + other.getX(),
			getY() + other.getY());
	}

	@Override
	public String toString()
	{
		return "Vector(x=" + getX() + ", y=" + getY() + ")";
	}

	/**
	 * equals() compares two coordinates to see if they indicate the same point.
	 *
	 * @param obj the other coordinate
	 * @return a boolean saying whether this represents the same coordinate
	 * as this
	 * @implNote we don't respect the symmetric property of the equivalence
	 * relation. It would be nonsense to say that a coordinate is different
	 * from a Mutable coordinate just because they are not exactly the same type
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Vector2D vector2D))
			return false;

		return vector2D.getY().equals(getY()) && getX().equals(vector2D.getX());
	}

	/**
	 * multiplication of coordinate by a scalar as if it was a vector
	 *
	 * @param i scalar by which coordinates must be multiplied
	 * @return the coordinate after multiplication
	 */
	public Vector2D multiply(int i)
	{
		return Vector2D.of(getX() * i, getY() * i);
	}

	/**
	 * vector sum of coordinates
	 *
	 * @param coordinate the other coordinate that must be subtracted from this
	 * @return a new coordinate that has as x attribute the subtraction of
	 * this.x and coordinate.getX() and the same thing happens for
	 * the y attribute
	 */
	public Vector2D sub(Vector2D other)
	{
		return Vector2D.of(getX() - other.getX(),
			getY() - other.getY());
	}

	/**
	 * division of coordinate by a scalar as if it was a vector
	 *
	 * @param i scalar by which coordinates must be divided
	 * @return the coordinate after multiplication
	 */
	public Vector2D div(int i)
	{
		return Vector2D.of(getX() / i, getY() / i);
	}

	/**
	 * modulus operation by a scalar as if this was a vector
	 *
	 * @param i the argument of %
	 * @return the coordinate after % operation
	 */
	public Vector2D mod(int i)
	{
		return Vector2D.of(getX() % i, getY() % i);
	}

	/**
	 * is the coordinate (0, 0)
	 *
	 * @return a boolean that says whether the coordinate is the origin of
	 * the axis
	 */
	public boolean isZero()
	{
		return getX() == 0 && getY() == 0;
	}

	private static final Map<Integer, Map<Integer, Vector2D>> pool = new HashMap<>();

	public static Vector2D of(int x, int y)
	{
		if (!pool.containsKey(x))
			pool.put(x, new HashMap<>());

		Map<Integer, Vector2D> innerMap = pool.get(x);
		if (!innerMap.containsKey(y))
			innerMap.put(y, new Vector2D(x, y));

		return innerMap.get(y);
	}

	public static Vector2D getIdentity()
	{
		return Vector2D.of(0, 0);
	}

	public Vector2D elementWiseApply(Vector2D otherVector,
																	 BinaryOperator<Integer> function)
	{
		return Vector2D.of(function.apply(getX(), otherVector.getX()),
			function.apply(getY(), otherVector.getY()));
	}
}
