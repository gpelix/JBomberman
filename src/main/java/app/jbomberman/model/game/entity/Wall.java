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
import app.jbomberman.model.game.entity.state.AbstractState;
import app.jbomberman.model.game.entity.state.StateFul;
import app.jbomberman.model.game.entity.state.WallAliveState;
import app.jbomberman.model.game.entity.state.WallDeadState;
import app.jbomberman.util.Vector2D;

/**
 * Wall is a class representing a Wall that disappears after an explosion in
 * proximity
 *
 * @author gp
 */
public class Wall extends Entity implements StateFul
{
	/**
	 * AbstractState is the state in which the entity is in
	 */
	private AbstractState state;

	/**
	 * @param coordinate  the model-coordinate of the Entity
	 * @param gameGrid    the GameGrid to which the Entity is attached
	 * @param aliveImages the ImageManager handling the images when the Wall is
	 *                    still safe and sound
	 * @param deathImages the ImageManager hadling the images when the Wall has
	 *                    been hit
	 */
	public Wall(Vector2D coordinate, GameGrid gameGrid,
							ImageManager aliveImages, ImageManager deathImages)
	{
		super(coordinate, gameGrid);

		state = new WallAliveState(this, new WallDeadState(this,
			deathImages), aliveImages);
	}

	/**
	 * updateInModel() calls the updateInModel() method of the state
	 */
	@Override
	public void updateInModel()
	{
		state.updateInModel();
	}

	/**
	 * notifyExplosion() calls the notifyExplosion() method of the state
	 *
	 * @return what notifyExplosion() method of the state returns
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return state.notifyExplosion(explosion);
	}

	/**
	 * allowsBombs() returns true if and only is the state is WallAliveState
	 *
	 * @return true if and only if the state is WallAliveState
	 */
	@Override
	public boolean allowsBombs()
	{
		return !(state instanceof WallAliveState);
	}

	/**
	 * getDrawing() calls the getDrawing() method of the state
	 *
	 * @return what gets returned by the getDrawing() on the state
	 */
	@Override
	public Drawing getDrawing()
	{
		return state.getDrawing();
	}

	/**
	 * setState() sets the state of the Wall
	 *
	 * @param state the state to set
	 */
	public void setState(AbstractState state)
	{
		this.state = state;
	}

	/**
	 * isDisappearing() calls the isDisappearing() on the state
	 *
	 * @return what gets returned by the isDisappearing() method of the state
	 */
	public boolean isDisappearing()
	{
		return state.isDisappearing();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
