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
 
package app.jbomberman.model.game.entity;

import app.jbomberman.util.Vector2D;

/**
 * Explosion is a class that models an explosion that is propagating on the
 * GameGrid
 *
 * @author gp
 */
public class Explosion
{
	/**
	 * strength is the number of freeCell the Explosion must still be propagated
	 * to
	 */
	private final int strength;

	/**
	 * fromGridCoordinate is the coordinate from which the explosion is
	 * propagating
	 */
	private final Vector2D fromGridCoordinate;

	/**
	 * public constructor of Explosion
	 *
	 * @param strength           the number of freeCell the Explosion must still
	 *                           be propagated to
	 * @param fromGridCoordinate the grid-coordinate from which the Explosion was
	 *                           propagated
	 */
	public Explosion(int strength, Vector2D fromGridCoordinate)
	{
		this.strength = strength;
		this.fromGridCoordinate = fromGridCoordinate;
	}

	/**
	 * getStrength() gets the strength of the explosion
	 *
	 * @return the strength of the explosion
	 */
	public int getStrength()
	{
		return strength;
	}

	/**
	 * getFromGridCoordinate() gets the coordinate from which the Explosion was
	 * propagated
	 *
	 * @return the coordinate from which the Explosion was propagated
	 */
	public Vector2D getFromGridCoordinate()
	{
		return fromGridCoordinate;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
