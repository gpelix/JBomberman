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

/**
 * Direction is an enum that represents the four possible cardinal points and
 * the only possible moves of an element in a grid
 *
 * @author gp
 */
public enum Direction
{
	/**
	 * NORTH direction
	 */
	NORTH(Vector2D.of(0, -1)),
	/**
	 * EAST direction
	 */
	EAST(Vector2D.of(1, 0)),
	/**
	 * SOUTH direction
	 */
	SOUTH(Vector2D.of(0, 1)),
	/**
	 * WEST direction
	 */
	WEST(Vector2D.of(-1, 0));

	/**
	 * A versor associated with the direction
	 */
	private final Vector2D associatedVersor;

	/**
	 * Create an instance of a Direction specifying the versor that must be
	 * associated to it
	 *
	 * @param associatedVersor the versor that must be associated to the
	 *                         enum
	 */
	Direction(Vector2D associatedVersor)
	{
		this.associatedVersor = associatedVersor;
	}

	/**
	 * get the versor
	 *
	 * @return the associated versor
	 */
	public Vector2D getVersor()
	{
		return associatedVersor;
	}

	/**
	 * check is the direction is EAST/WEST
	 *
	 * @return whether the direction is EAST/WEST
	 */
	public boolean isHorizontal()
	{
		return this == EAST || this == WEST;
	}

	/**
	 * check is the direction is NORTH/SOUTH
	 *
	 * @return whether the direction is NORTH/SOUTH
	 */
	@SuppressWarnings("unused")
	public boolean isVertical()
	{
		return this == NORTH || this == SOUTH;
	}

	/**
	 * get a direction from a vector indicating that
	 *
	 * @param vector a vector that points in a certain direction
	 * @return a maybe object that contains the direction if the vector indicated
	 * a valid direction or nothing if the vector is askew
	 */
	public static Maybe<Direction> fromDirectionVector(Vector2D vector)
	{
		int x = vector.getX();
		int y = vector.getY();

		return Maybe.something(
			x > 0 ? EAST : x < 0 ? WEST : y > 0 ? SOUTH : y < 0 ? NORTH : null);
	}

	/**
	 * get the opposite direction of this
	 *
	 * @return the opposite direction of this
	 */
	public Direction opposite()
	{
		return switch (this) {
			case EAST -> WEST;
			case WEST -> EAST;
			case NORTH -> SOUTH;
			case SOUTH -> NORTH;
		};
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
