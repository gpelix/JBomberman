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
	 * public constructor from another GameCharacterCapabilities object
	 *
	 * @param other the other GameCharacterCapabilities object
	 */
	public GameCharacterCapabilities(GameCharacterCapabilities other)
	{
		this(other.goesThroughWallsFlag, other.speed);
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
