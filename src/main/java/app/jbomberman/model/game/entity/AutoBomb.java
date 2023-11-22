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

import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

/**
 * AutoBomb is a Bomb that detonates automatically
 *
 * @author gp
 */
public class AutoBomb extends Bomb
{
	/**
	 * BOMB_EXPLOSION_INTERVAL is the number of updates needed before the automatic
	 * detonation of the bomb
	 */
	private static final int BOMB_EXPLOSION_INTERVAL = 240;

	/**
	 * timeToExplosion is the number of updates for the AutoBomb that are still
	 * needed before the detonation of the bomb
	 */
	private int timeToExplosion = BOMB_EXPLOSION_INTERVAL;

	/**
	 * public constructor
	 *
	 * @param coordinate   the model-coordinate of the Entity
	 * @param gameGrid     the GameGrid to which the Entity is attached
	 * @param bombRange    the range of the bomb
	 * @param bomberMan    the bomberman that spawned the bomb
	 * @param imageManager the ImageManager responsible for displaying the images
	 */
	public AutoBomb(Vector2D coordinate, int bombRange,
									BomberMan bomberMan, ImageManager imageManager,
									GameGrid gameGrid)
	{
		super(coordinate, bombRange, bomberMan, imageManager, gameGrid);
	}

	/**
	 * updateInModel() does nothing until timeToExplosion reaches zero
	 */
	@Override
	public void updateInModel()
	{
		if (--timeToExplosion == 0)
			updateWithExplosion();
	}

	/**
	 * toString() is only used for debugging purpose
	 *
	 * @return the String representation of the Entity
	 */
	@Override
	public String toString()
	{
		return "AutoBomb(" + super.toString() + ")";
	}

	/**
	 * makeExplode() does nothing, that is it simply ignores the requests to
	 * explode
	 */
	@Override
	public void makeExplode()
	{
		// Do nothing
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
