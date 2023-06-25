package app.jbomberman.model;

import app.jbomberman.model.game.entity.BomberManCapabilities;

/**
 * Account is the class that model an account
 *
 * @author gp
 */
public class Account
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
	 * capabilities are the capabilities that the player has acquired playing
	 */
	private BomberManCapabilities capabilities;

	/**
	 * public constructor of an Account from a name, a level, and the player's
	 * capabilities
	 *
	 * @param name            the login name of the Account
	 * @param level           the level that the player has reached
	 * @param hisCapabilities the capabilities that the player's bomberman must
	 *                        have
	 */
	public Account(String name, int level,
								 BomberManCapabilities hisCapabilities)
	{
		this.playerName = name;
		this.level = level;
		this.capabilities = hisCapabilities;
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
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
