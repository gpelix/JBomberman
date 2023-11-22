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

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.util.Direction;

/**
 * EnemyAliveState is the class that represents the state of an Enemy alive
 *
 * @author gp
 */
public class EnemyAliveState extends AliveState
{
	/**
	 * enemy that has this as state
	 */
	private final Enemy enemy;

	/**
	 * imageManager is enemy's ImageManager
	 */
	private final MovingImageManager imageManager;

	/**
	 * public constructor of EnemyAliveState
	 *
	 * @param enemy        the Enemy that has this as state
	 * @param imageManager the ImageManager of the Enemy
	 * @param nextState    the state to set when the Enemy dies
	 */
	public EnemyAliveState(Enemy enemy,
												 MovingImageManager imageManager,
												 AbstractState nextState)
	{
		super(enemy, nextState);
		this.enemy = enemy;
		this.imageManager = imageManager;
	}

	/**
	 * notifyExplosion() must be called to notify the Enemy that there has been an
	 * explosion
	 *
	 * @param explosion the Explosion object
	 * @return false as enemies don't block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		Model.getInstance().getAccount().getCapabilities().addPoints(enemy.getDeathPoints());
		die();
		return false;
	}

	/**
	 * updateInModel() moves the enemy
	 */
	@Override
	public void updateInModel()
	{
		enemy.move();
	}

	/**
	 * updateInModel() gets the Drawing of the Enemy.
	 *
	 * @return the Drawing of the Enemy
	 */
	@Override
	public Drawing getDrawing()
	{
		// enemies always move and always change there image
		enemy.hasChanged();
		return enemy.getDrawing(imageManager.getImage(enemy.getPreviousMoveDirection().orElseWrapped(Direction.NORTH)));
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
