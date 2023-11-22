/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
package app.jbomberman.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

/**
 * Vector2D is a class that represents a two-dimensional vector. It is a kind
 * of pair but with added operations which are typical of vectors like multiplication
 * by a scalar value. Since a lot of those vectors will be around, we implement
 * their creation using the FlightWeight pattern and we make them immutable.
 *
 * @author gp
 */
public class Vector2D extends Pair<Integer, Integer>
{
	/**
	 * pool is the pool of Vector2D's used for caching
	 */
	private static final Map<Integer, Map<Integer, Vector2D>> pool = new HashMap<>();

	/**
	 * of() is a static method that must be called to get a Vector2D
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the Vector2D with the coordinates specified as parameters
	 */
	public static Vector2D of(int x, int y)
	{
		if (!pool.containsKey(x))
			pool.put(x, new HashMap<>());

		Map<Integer, Vector2D> innerMap = pool.get(x);
		if (!innerMap.containsKey(y))
			innerMap.put(y, new Vector2D(x, y));

		return innerMap.get(y);
	}

	/**
	 * protected constructor that builds a 2D vector from a pair of values
	 * It must not be called by other classes that want to instantiate a
	 * Vector2D (except for subclasses) as they have to call .of() method of
	 * Vector2D
	 *
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
	 * @param other the other Vector2D that must be summed to this
	 * @return a new coordinate that has as x attribute the sum of this.x and
	 * other.getX() and the same thing happens for the y attribute
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
	 * multiplication of vector by a scalar
	 *
	 * @param i scalar by which the vector must be multiplied
	 * @return the vector after multiplication
	 */
	public Vector2D multiply(int i)
	{
		return Vector2D.of(getX() * i, getY() * i);
	}

	/**
	 * vector sum of vector
	 *
	 * @param other the other vector that must be subtracted from this
	 * @return a new vector that has as x attribute the subtraction of
	 * this.x and other.getX() and the same thing happens for
	 * the y attribute
	 */
	public Vector2D sub(Vector2D other)
	{
		return Vector2D.of(getX() - other.getX(),
			getY() - other.getY());
	}

	/**
	 * division of vector by a scalar
	 *
	 * @param i scalar by which the vector must be divided
	 * @return the vector after multiplication
	 */
	public Vector2D div(int i)
	{
		return Vector2D.of(getX() / i, getY() / i);
	}

	/**
	 * modulus operation by a scalar
	 *
	 * @param i the argument of %
	 * @return the vector after % operation
	 */
	public Vector2D mod(int i)
	{
		return Vector2D.of(getX() % i, getY() % i);
	}

	/**
	 * @return a boolean that says whether the vector is the origin of
	 * the axis
	 */
	public boolean isZero()
	{
		return getIdentity().equals(this);
	}

	/**
	 * @return the origin of the axis
	 */
	public static Vector2D getIdentity()
	{
		return Vector2D.of(0, 0);
	}

	/**
	 * elementWiseApply applies a BinaryOperator to this.x and otherVector.x
	 * to create the new x coordinate of the result. The same is done for the y
	 * attribute
	 *
	 * @param otherVector the other vector used to compute the result
	 * @param function    function of this.x and otherVector.x to determine the value
	 *                    of the x coordinate of the vector to return. The same gets
	 *                    done on the y coordinate
	 * @return the vector whose coordinates are the result of the application of
	 * function on the coordinate of this and otherVector
	 */
	public Vector2D elementWiseApply(Vector2D otherVector,
																	 BinaryOperator<Integer> function)
	{
		return Vector2D.of(function.apply(getX(), otherVector.getX()),
			function.apply(getY(), otherVector.getY()));
	}

  /**
   * equals() compares two coordinates to see if they indicate the same point.
   * <p>
   * we don't respect the symmetric property of the equivalence
   * relation. It would be nonsense to say that a coordinate is different
   * from a MutableVector2D just because they are not exactly the same type
   *
   * @param obj the other vector
   * @return a boolean saying whether this represents the same vector
   * as this
   */
  @Override
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Vector2D vector2D))
      return false;

    return vector2D.getY().equals(getY()) && getX().equals(vector2D.getX());
  }

}

/* vim: set ts=2 sw=2 tw=0 noet :*/
