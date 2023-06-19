
package app.jbomberman.model.game.entity;

import app.jbomberman.util.Vector2D;

public class Explosion
{
	private final int strength;
	private final Vector2D fromGridCoordinate;

	public Explosion(int strength, Vector2D fromGridCoordinate)
	{
		this.strength = strength;
		this.fromGridCoordinate = fromGridCoordinate;
	}

	public int getStrength()
	{
		return strength;
	}

	public Vector2D getFromGridCoordinate()
	{
		return fromGridCoordinate;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
