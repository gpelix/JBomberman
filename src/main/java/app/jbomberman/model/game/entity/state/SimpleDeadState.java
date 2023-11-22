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
import app.jbomberman.image.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.GamePlayException;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.Wall;

/**
 * SimpleDeadState is the DeadState of entities whose death is just a fading
 *
 * @param <T> the specific type of the Stateful
 * @author gp
 */
public class SimpleDeadState<T extends Entity & StateFul> extends DeadState
{
	/**
	 * deathImageManager is the ImageManager that will display the images
	 */
	private final ImageManager deathImageManager;

	/**
	 * public constructor of SimpleDeadState from the Stateful entity and
	 * ImageManager
	 *
	 * @param entity            the Entity whose state is this
	 * @param deathImageManager the ImageManager that will handle the death
	 *                          sequence
	 */
	public SimpleDeadState(T entity, ImageManager deathImageManager)
	{
		super(entity);
		this.deathImageManager = deathImageManager;
	}

	/**
	 * getDrawing() gets the image from the ImageManager
	 *
	 * @return the image representing the dying enemy
	 */
	@Override
	public Drawing getDrawing()
	{
		AbstractImage image = deathImageManager.getImage();
		if (deathImageManager.hasChanged())
			getEntity().hasChanged();
		return getEntity().getDrawing(image);
	}

	/**
	 * updateInModel() removes the Entity from the Grid if the ImageManager has
	 * done one cycle
	 */
	@Override
	public void updateInModel()
	{
		Entity entity = getEntity();

		if (getDeathImageManager().hasDoneOneCycle()) {
			GameGrid gameGrid = entity.getGameGrid();

			if (entity instanceof Wall wall)
				gameGrid.get(
					gameGrid.getCenterInGridCoord(entity)
				).removeWall(wall);
			else if (entity instanceof Enemy enemy)
				gameGrid.get(
					gameGrid.getCenterInGridCoord(enemy)
				).removeCharacter(enemy);
			else
				throw new GamePlayException(
					"What is this. What should I do with" + entity.getClass());
		}
	}

	/**
	 * getEntity() gets the underlying Stateful casting it to an Entity
	 *
	 * @return the Entity whose state is this
	 */
	protected Entity getEntity()
	{
		return (Entity) super.getStateful();
	}

	/**
	 * getDeathImageManager() must be used to get the ImageManager from subclasses
	 *
	 * @return the ImageManager
	 */
	protected ImageManager getDeathImageManager()
	{
		return deathImageManager;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
