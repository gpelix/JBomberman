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

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

/**
 * Fire is an entity that represents the result of an explosion
 *
 * @author gp
 */
public class Fire extends Entity
{
	/**
	 * FireType is the type of the fire. (Different images for different type of
	 * fire)
	 *
	 * @author gp
	 */
	public enum FireType
	{
		/**
		 * CENTER is the source of the fire
		 */
		CENTER,
		/**
		 * HORIZONTAL is a kind of fire extending horizontally
		 */
		HORIZONTAL,
		/**
		 * VERTICAL is a kind of fire extending vertically
		 */
		VERTICAL,
		/**
		 * NORTH is the northern tip of the surge
		 */
		NORTH,
		/**
		 * SOUTH is the southern tip of the surge
		 */
		SOUTH,
		/**
		 * EAST is the eastern tip of the surge
		 */
		EAST,
		/**
		 * WEST is the western tip of the surge
		 */
		WEST
	}

	/**
	 * imageManager is the ImageManager for the Fire
	 */
	private final ImageManager imageManager;

	/**
	 * protected constructor meant to be called by subclasses' constructors to
	 * initialize the Fire from its model-coordinate, the GameGrid to which it
	 * is attached and its ImageManager
	 *
	 * @param coordinate   the model-coordinate of the Fire
	 * @param gameGrid     the GameGrid to which the Fire is attached
	 * @param imageManager the ImageManager for the Fire
	 */
	public Fire(Vector2D coordinate, GameGrid gameGrid, ImageManager imageManager)
	{
		super(coordinate, gameGrid);

		this.imageManager = imageManager;
	}

	/**
	 * getDrawing() returns the old Drawing if the image hasn't Changed, otherwise
	 * it returns the new one
	 *
	 * @return the new Drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		if (imageManager.hasChanged())
			hasChanged();
		return getDrawing(imageManager.getImage());
	}

	/**
	 * updateInModel() does nothing until the ImageManager has done one cycle of
	 * images
	 */
	@Override
	public void updateInModel()
	{
		if (imageManager.hasDoneOneCycle()) {
			FreeCell freeCell = getUnderlyingFreeCell();
			freeCell.removeFire(this);
		}
	}

	/**
	 * notifyExplosion() returns false as the Fire doesn't Block explosions
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return false
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return false;
	}

	/**
	 * Fire allows bombs
	 *
	 * @return true as fire always allows bombs
	 */
	@Override
	public boolean allowsBombs()
	{
		return true;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
