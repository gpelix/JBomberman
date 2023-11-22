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
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.model.game.entity.Wall;

/**
 * WallAliveState is the alive state of Walls
 *
 * @author gp
 */
public class WallAliveState extends AliveState
{
	/**
	 * wall is a reference to the Wall that has this as state
	 */
	private final Wall wall;

	/**
	 * imageManager is wall's ImageManager
	 */
	private final ImageManager imageManager;

	/**
	 * public constructor of WallAliveState
	 *
	 * @param wall         the Wall that has this as state
	 * @param imageManager the ImageManager of the Wall
	 * @param nextState    the state to set when the Wall dies
	 */
	public WallAliveState(Wall wall, AbstractState nextState,
												ImageManager imageManager)
	{
		super(wall, nextState);
		this.wall = wall;
		this.imageManager = imageManager;
	}

	/**
	 * notifyExplosion() must be called to notify the Wall that there has been
	 * an explosion and the Wall gets destroyed
	 *
	 * @param explosion the Explosion object
	 * @return true as Walls block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		die();
		return true;
	}

	/**
	 * updateInModel() does nothing as Walls do not require any update
	 */
	@Override
	public void updateInModel()
	{
		// Do nothing
	}

	/**
	 * getDrawing() simply gets the only image in the ImageManager
	 *
	 * @return the Image of the Wall
	 */
	@Override
	public Drawing getDrawing()
	{
		return wall.getDrawing(imageManager.getImage());
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
