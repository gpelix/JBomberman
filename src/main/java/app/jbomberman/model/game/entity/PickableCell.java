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

import app.jbomberman.image.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

import java.util.function.Supplier;

/**
 * PickableCell is a class modeling every kind of Cell that can be picked by
 * BomberMan
 *
 * @author gp
 */
public abstract class PickableCell extends Cell
{
	/**
	 * enemySupplier is a Supplier that constructs an Enemy when requested
	 */
	private final Supplier<Enemy> enemySupplier;

	/**
	 * public constructor of PickableCell
	 *
	 * @param coordinate    the model-coordinate of the PiackbleCell
	 * @param gameGrid      the GameGrid to which the PickableCell is attached
	 * @param image         the image of the PickableCell
	 * @param enemySupplier the supplier that will construct the Enemy when
	 *                      requested
	 */
	public PickableCell(Vector2D coordinate, GameGrid gameGrid,
											AbstractImage image, Supplier<Enemy> enemySupplier)
	{
		super(coordinate, image, gameGrid);

		this.enemySupplier = enemySupplier;
	}

	/**
	 * updateInModel() does nothing as the PickableCell never changes state
	 */
	@Override
	public final void updateInModel()
	{
		// Do nothing
	}

	/**
	 * allowsBombs() always returns false as PickableCells don't allow Bomberman
	 * placing bombs on them
	 *
	 * @return false
	 */
	@Override
	public boolean allowsBombs()
	{
		return false;
	}

	/**
	 * notifyExplosion spawns the enemy got from the enemySupplier
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return true as PickableCells always block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		getGameGrid().addGameCharacter(enemySupplier.get(),
			getGameGrid().getCenterInGridCoord(this));

		return true;
	}

	/**
	 * pick() must be called when the PickableCell gets picked by Bomberman
	 *
	 * @param bomberMan the instance of Bomberman that picked the PickableCell
	 */
	public abstract void pick(BomberMan bomberMan);
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
