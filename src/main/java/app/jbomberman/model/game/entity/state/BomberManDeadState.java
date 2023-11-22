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
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.BomberMan;

/**
 * BomberManDeadState is the state of BomberMan when he's dead
 *
 * @author gp
 */
public class BomberManDeadState extends DeadState
{
	/**
	 * bomberMan is a reference to the bomberMan that has this as state
	 */
	private final BomberMan bomberMan;

	/**
	 * deathImageManager is bomberman's ImageManager
	 */
	private final ImageManager deathImageManager;

	/**
	 * public constructor of BomberManDeadState
	 *
	 * @param bomberMan         the bomberman whose state is this one
	 * @param deathImageManager bomberman's ImageManager
	 */
	public BomberManDeadState(BomberMan bomberMan,
														ImageManager deathImageManager)
	{
		super(bomberMan);
		this.bomberMan = bomberMan;
		this.deathImageManager = deathImageManager;
	}

	/**
	 * updateInModel() waits until the death sound has ended and then ends the game
	 */
	@Override
	public void updateInModel()
	{
		if (bomberMan.getSoundHandle().fromJust().getSoundFileName().equals(BomberMan.getBombermanDeathSound()) &&
			deathImageManager.hasDoneOneCycle())
		{
			GameGrid gameGrid = bomberMan.getGameGrid();
			gameGrid.get(
				gameGrid.getCenterInGridCoord(bomberMan)
			).removeCharacter(bomberMan);
			Model.getInstance().getGame().endGame(false);
		}
	}

	/**
	 * getDrawing() gets the BomberMan's Drawing
	 *
	 * @return the Drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		AbstractImage image = deathImageManager.getImage();
		if (deathImageManager.hasChanged())
			bomberMan.hasChanged();
		return bomberMan.getDrawing(image);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
