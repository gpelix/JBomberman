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

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

/**
 * Powerup is a PickableCell that enhances the abilities bomberman has
 *
 * @author gp
 */
public class Powerup extends PickableCell
{
	/**
	 * POWERUP_TAKEN_MUSIC is the sound that must be played when the powerup gets
	 * taken
	 */
	private static final String POWERUP_TAKEN_MUSIC =
		Properties.getInstance().getSoundPath("Bomberman SFX powerup picked.wav");

	/**
	 * deltaCapabilities are the added capabilities that taking this Powerup will
	 * give to BomberMan
	 */
	private final BomberManCapabilities deltaCapabilities;

	/**
	 * public constructor of Powerup
	 *
	 * @param coordinate        the model-coordinate of the Powerup
	 * @param gameGrid          the GameGrid to which the Powerup is attached
	 * @param image             the image of the PowerUp
	 * @param enemyToSpawn      the enemy to spawn on request
	 * @param deltaCapabilities the extra capabilities the Bomberman will receive
	 *                          when this PowerUp gets taken
	 */
	public Powerup(Vector2D coordinate, GameGrid gameGrid,
								 AbstractImage image, Enemy enemyToSpawn,
								 BomberManCapabilities deltaCapabilities)
	{
		super(coordinate, gameGrid, image, () -> enemyToSpawn);

		this.deltaCapabilities = deltaCapabilities;
	}

	/**
	 * notifyExplosion removes the powerup from the grid and lets the
	 * PickableCell.notifyExplosion() handle the remaining things
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return true as PickableCells always block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		getUnderlyingFreeCell().removePowerup(this);
		return super.notifyExplosion(explosion);
	}

	/**
	 * pick() gets the PowerUp removing it from the grid and updating the
	 * bomberman's capabilities
	 *
	 * @param bomberMan the instance of Bomberman that picked the PickableCell
	 */
	@Override
	public void pick(BomberMan bomberMan)
	{
		getUnderlyingFreeCell().removePowerup(this);
		AudioPlayer.getInstance().playSound(POWERUP_TAKEN_MUSIC);
		Model.getInstance().getGame().powerUpTaken();
		bomberMan.getCapabilities().addDelta(deltaCapabilities);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
