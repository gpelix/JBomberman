package app.jbomberman.model;

import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.util.Maybe;

import java.nio.file.Path;

/**
 * Player is the class that model an account
 *
 * @author gp
 */
public class Player
{
	/**
	 * playerName is the name of the player
	 */
	private final String playerName;

	/**
	 * level is the level number
	 */
	private int level;

	/**
	 * avatarPath is the path of the avatar
	 */
	private Maybe<Path> avatarPath = Maybe.nothing();

	/**
	 * capabilities are the capabilities that the player has acquired playing
	 */
	private BomberManCapabilities capabilities;

	/**
	 * public constructor of an Player from a name, a level, and the player's
	 * capabilities
	 *
	 * @param name            the login name of the Player
	 * @param level           the level that the player has reached
	 * @param avatarPath      the path of the avatar
	 * @param hisCapabilities the capabilities that the player's bomberman must
	 *                        have
	 */
	public Player(String name, int level, Maybe<Path> avatarPath,
								BomberManCapabilities hisCapabilities)
	{
		this.playerName = name;
		this.level = level;
		this.capabilities = hisCapabilities;
		this.avatarPath = avatarPath;
	}

	/**
	 * getCapabilities() must be used to get the capabilities of the player
	 *
	 * @return the player's capabilities
	 */
	public BomberManCapabilities getCapabilities()
	{
		return capabilities;
	}

	/**
	 * getLevel() gets the level the player has reached
	 *
	 * @return the level the player has reached
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * getPlayerName() returns the login name associated with the account
	 *
	 * @return the login name associated with the account
	 */
	public String getPlayerName()
	{
		return playerName;
	}

	/**
	 * setCapabilities() sets the capabilities of the account to the ones
	 * given as input
	 *
	 * @param capabilities the new capabilities
	 */
	public void setCapabilities(BomberManCapabilities capabilities)
	{
		this.capabilities = capabilities;
	}

	/**
	 * increment level is used to increment the level the player has reached
	 */
	public void incrementLevel()
	{
		level++;
	}

	/**
	 * setLevel() sets the level the player has reached
	 *
	 * @param level the level to set
	 */
	public void setLevel(int level)
	{
		this.level = level;
	}

	/**
	 * setAvatar() must be used to set the avatar
	 *
	 * @param path the path of the avatar
	 */
	public void setAvatar(Path path)
	{
		avatarPath = Maybe.just(path);
	}

	/**
	 * getAvatar() gets the path of the avatar
	 *
	 * @return the path of the avatar
	 */
	public Maybe<Path> getAvatar()
	{
		return avatarPath;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
