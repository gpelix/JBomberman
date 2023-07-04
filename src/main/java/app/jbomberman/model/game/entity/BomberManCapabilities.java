package app.jbomberman.model.game.entity;

import app.jbomberman.util.Maybe;

import java.nio.file.Path;

/**
 * BomberManCapabilities is the class that represents the capabilities
 * bomberman has
 *
 * @author gp
 */
public class BomberManCapabilities extends GameCharacterCapabilities
{
	/**
	 * bombPower is an integer describing the range that the BomberMan's bombs
	 * have
	 */
	private int bombPower;
	/**
	 * availableBombs is the number of the bombs that the BomberMan can spawn at
	 * the same time
	 */
	private int availableBombs;
	/**
	 * remoteControl is a boolean that is true if and only if the BomberMan has
	 * remote control for bombs
	 */
	private boolean remoteControl;
	/**
	 * goesThroughBombs is a boolean set to true if and only if the BomberMan can
	 * go through bombs
	 */
	private boolean goesThroughBombs;
	/**
	 * goesThroughFire is a boolean set to true if and only if the BomberMan can
	 * go through fire
	 */
	private boolean goesThroughFire;
	/**
	 * isInvincible is true if and only if the BomberMan is invincible
	 */
	private boolean isInvincible;
	/**
	 * score is the score that the BomberMan has accumulated
	 */
	private int score;
	/**
	 * numberOfLives is the number of lives that the BomberMan still has
	 */
	private int numberOfLives;

	/**
	 * defaultCapabilities are the default capabilities of BomberMan
	 */
	public static final BomberManCapabilities defaultCapabilities =
		new BomberManCapabilities(
			0.6,
			2,
			1,
			false,
			false,
			false,
			false,
			false,
			0,
			3,
			Maybe.nothing()
		);

	/**
	 * avatarPath is the path of the avatar
	 */
	private Maybe<Path> avatarPath;

	/**
	 * @param speed            the speed of the BomberMan
	 * @param bombPower        the range that the BomberMan's bombs  have
	 * @param availableBombs   number of the bombs that the BomberMan can spawn at
	 *                         the same time
	 * @param remoteControl    remoteControl is a boolean that is true if and only
	 *                         if the BomberMan has  remote control for bombs
	 * @param goesThroughWalls boolean set if and only if BomberMan goes through
	 *                         walls
	 * @param goesThroughBombs boolean set to true if and only if the BomberMan
	 *                         can go through bombs
	 * @param goesThroughFire  a boolean set to true if and only if the BomberMan
	 *                         can  go through fire
	 * @param isInvincible     rue if and only if the BomberMan is invincible
	 * @param score            score is the score that the BomberMan has accumulated
	 * @param numberOfLives    the number of lives that the BomberMan still has
	 */
	public BomberManCapabilities(double speed,
															 int bombPower, int availableBombs,
															 boolean remoteControl,
															 boolean goesThroughWalls,
															 boolean goesThroughBombs,
															 boolean goesThroughFire,
															 boolean isInvincible,
															 int score,
															 int numberOfLives,
															 Maybe<Path> avatarPath)
	{
		super(goesThroughWalls, speed);

		this.bombPower = bombPower;
		this.availableBombs = availableBombs;
		this.remoteControl = remoteControl;
		this.goesThroughBombs = goesThroughBombs;
		this.goesThroughFire = goesThroughFire;
		this.isInvincible = isInvincible;
		this.score = score;
		this.numberOfLives = numberOfLives;
		this.avatarPath = avatarPath;
	}

	/**
	 * getBombPower() gets the bomb range
	 *
	 * @return the bomb range
	 */
	public int getBombPower()
	{
		return bombPower;
	}

	/**
	 * getAvailableBombs() gets the number of available bombs
	 *
	 * @return the number of available bombs
	 */
	public int getAvailableBombs()
	{
		return availableBombs;
	}

	/**
	 * hasRemoteControl() returns whether BomberMan has remote control
	 *
	 * @return whether BomberMan has remote control
	 */
	public boolean hasRemoteControl()
	{
		return remoteControl;
	}

	/**
	 * goesThroughFire() returns whether BomberMan can go through fire
	 *
	 * @return whether BomberMan can go through fire
	 */
	public boolean goesThroughFire()
	{
		return goesThroughFire;
	}

	/**
	 * goesThroughBombs() returns the goesThroughBombsFlag
	 *
	 * @return if the BomberMan goes through bombs
	 */
	public boolean goesThroughBombs()
	{
		return goesThroughBombs;
	}

	/**
	 * isInvincible() returns true only if BomberMan is invincible
	 *
	 * @return true only if BomberMan is invincible
	 */
	public boolean isInvincible()
	{
		return isInvincible;
	}

	/**
	 * unsetIsInvincible() removes the invincibility of BomberMan
	 */
	public void unsetIsInvincible()
	{
		isInvincible = false;
	}

	/**
	 * setNumberOfLives() sets the number of lives of BomberMan
	 *
	 * @param numberOfLives the number of lives of BomberMan to set
	 */
	public void setNumberOfLives(int numberOfLives)
	{
		this.numberOfLives = numberOfLives;
	}

	/**
	 * addDelta() adds the delta capabilities to the current ones
	 *
	 * @param capabilities the deltaCapabilities
	 */
	public void addDelta(BomberManCapabilities capabilities)
	{
		setSpeed(getSpeed() + capabilities.getSpeed());
		bombPower += capabilities.getBombPower();
		availableBombs += capabilities.availableBombs;
		remoteControl |= capabilities.remoteControl;
		setGoesThroughWalls(goesThroughWalls() || capabilities.goesThroughWalls());
		goesThroughBombs |= capabilities.goesThroughBombs;
		goesThroughFire |= capabilities.goesThroughFire;
		isInvincible |= capabilities.isInvincible;
		score += capabilities.score;
		numberOfLives += capabilities.numberOfLives;
	}

	/**
	 * takesPowerUps() returns whether BomberMan takes powerups.
	 * In this case, it's always true
	 *
	 * @return true
	 */
	@Override
	public boolean takesPowerUps()
	{
		return true;
	}

	/**
	 * toString() is only used for debugging purpose
	 *
	 * @return the String representation of the Entity
	 */
	@Override
	public String toString()
	{
		return ("BomberManCapabilities(%s, bombPower=%d, availableBombs=%d, " +
			"remoteControl=%b, goesThroughBombs=%b, goesThroughFire=%b, " +
			"isInvincible=%b)")
			.formatted(super.toString(), bombPower, availableBombs, remoteControl,
				goesThroughBombs, goesThroughFire, isInvincible);
	}

	/**
	 * getNumberOfLives() returns the number of lives available
	 *
	 * @return the number of lives available
	 */
	public int getNumberOfLives()
	{
		return numberOfLives;
	}

	/**
	 * getPointsEarned() returns the currently earn points
	 *
	 * @return the currently earned points
	 */
	public int getPointsEarned()
	{
		return score;
	}

	/**
	 * removeLife() removes one life to the ones currently available
	 */
	public void removeLife()
	{
		numberOfLives--;
	}

	/**
	 * addLife() adds one life to the ones currently available
	 */
	public void addLife()
	{
		numberOfLives++;
	}

	/**
	 * addPoints() adds the points specified in input to the points currently
	 * accumulated
	 *
	 * @param points the points to add
	 */
	public void addPoints(int points)
	{
		score += points;
	}

	public void setAvatarPath(Path path)
	{
		avatarPath = Maybe.just(path);
	}

	public Maybe<Path> getAvatar()
	{
		return avatarPath;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
