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
