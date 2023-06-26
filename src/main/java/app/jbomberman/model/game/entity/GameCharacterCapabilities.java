package app.jbomberman.model.game.entity;

import app.jbomberman.util.RandomNumberGenerator;

/**
 * GameCharacterCapabilities is a class that contains the capabilities of every
 * BomberManCharacter's
 *
 * @author gp
 */
public class GameCharacterCapabilities
{
	/**
	 * goesThroughtWallsFlag is a boolean that keeps track of whether the
	 * GameCharacter can go through walls
	 */
	private boolean goesThroughWallsFlag;

	/**
	 * speed is the speed of the GameCharacter
	 */
	private double speed;

	/**
	 * public constructor of GameCharacterCapabilities from goesThroughWallsFlag
	 * and a double representing the speed
	 *
	 * @param goesThroughWalls the doesThrought walls flag that is true if and only
	 *                         if the GameCharacter can go through walls
	 * @param speed            the speed of the GameCharacter
	 */
	public GameCharacterCapabilities(boolean goesThroughWalls, double speed)
	{
		this.speed = speed;
		this.goesThroughWallsFlag = goesThroughWalls;
	}

	/**
	 * getSpeed() gets the speed of the GameCharacter
	 *
	 * @return the speed of the GameCharacter
	 */
	public double getSpeed()
	{
		return speed;
	}

	/**
	 * getSpeedInstance() gets the speed plus a little bit of randomness
	 *
	 * @return the speed plus a little bit of randomness
	 */
	public double getSpeedInstance()
	{
		return speed + RandomNumberGenerator.getInstance().nextDouble() * speed / 4;

	}

	/**
	 * goesThroughWalls() return true if and only if the GameCharacter goes
	 * through walls
	 *
	 * @return true if and only if the GameCharacter goes through walls
	 */
	public boolean goesThroughWalls()
	{
		return goesThroughWallsFlag;
	}

	/**
	 * toString() is only used for debugging purpose
	 *
	 * @return the String representation of the Entity
	 */
	@Override
	public String toString()
	{
		return "GameCharacterCapabilities(speed=%f, goesThroughWalls=%b)"
			.formatted(speed, goesThroughWallsFlag);
	}


	/**
	 * takesPowerUps() return true if and only if the GameCharacter takes
	 * powerups
	 *
	 * @return true if and only if the GameCharacter takes powerups
	 */
	public boolean takesPowerUps()
	{
		return false;
	}

	/**
	 * setSpeed sets the speed
	 *
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	/**
	 * setGoesThroughWalls sets the goesThroughWallsFlag to the one specified in
	 * input
	 *
	 * @param goesThroughWallsFlag the boolean to assign to the
	 *                             goesThroughWallsFlag
	 */
	public void setGoesThroughWalls(boolean goesThroughWallsFlag)
	{
		this.goesThroughWallsFlag = goesThroughWallsFlag;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
