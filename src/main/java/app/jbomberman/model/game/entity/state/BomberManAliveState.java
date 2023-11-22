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
 
package app.jbomberman.model.game.entity.state;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.BomberMan;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.util.CollectionUtils;
import app.jbomberman.util.Maybe;

/**
 * BomberManAliveState is the state of BomberMan when it's alive
 *
 * @author gp
 */
public class BomberManAliveState extends AliveState
{
	/**
	 * bomberMan is a reference to the bomberMan that has this as state
	 */
	private final BomberMan bomberMan;

	/**
	 * imageManager is bomberman's ImageManager
	 */
	private final MovingImageManager imageManager;

	/**
	 * public constructor of BomberManAliveState
	 *
	 * @param bomberMan    the bomberman whose state is this one
	 * @param imageManager bomberman's ImageManager
	 * @param nextState    the state to set on BomberMan's death
	 */
	public BomberManAliveState(BomberMan bomberMan,
														 MovingImageManager imageManager,
														 AbstractState nextState)
	{
		super(bomberMan, nextState);
		this.bomberMan = bomberMan;
		this.imageManager = imageManager;
	}

	/**
	 * notifyExplosion() handles an explosion.
	 * If bomberman is invincible or can go through fire, it does nothing; otherwise
	 * it kills bomberman
	 *
	 * @param explosion the Explosion object
	 * @return false as bomberman doesn't stop explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		BomberManCapabilities capabilities = bomberMan.getCapabilities();
		if (!capabilities.goesThroughFire() && !capabilities.isInvincible())
			die();

		return false;
	}

	/**
	 * updateInModel() moves bomberman
	 */
	@Override
	public void updateInModel()
	{
		bomberMan.move();
	}

	/**
	 * getDrawing() returns the current bomberman's image.
	 * It simply returns the Drawing bomberman's getDrawing(AbstractImage)
	 * returns
	 *
	 * @return BomberMan's Drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		return bomberMan.getDrawing(imageManager.getImage(
			CollectionUtils.pick(bomberMan.getDirections())));
	}

	/**
	 * notifyCaughtByEnemy() must be called when bomberman gets caught by
	 * an enemy. If he's not invincible, he dies
	 */
	public void notifyCaughtByEnemy()
	{
		if (!bomberMan.getCapabilities().isInvincible())
			die();
	}

	/**
	 * die() makes bomberman die, removes a life from his, and play the death
	 * sound
	 */
	@Override
	protected void die()
	{
		super.die();

		Model.getInstance().getAccount().getCapabilities().removeLife();

		bomberMan.setSoundHandle(
			Maybe.just(AudioPlayer.getInstance().playSound(BomberMan.getBombermanDeathSound())));
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
