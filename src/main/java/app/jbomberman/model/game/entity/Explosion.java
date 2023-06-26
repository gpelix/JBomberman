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
