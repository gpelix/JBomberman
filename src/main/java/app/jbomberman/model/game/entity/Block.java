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

import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

/**
 * Block is a class that represents a Block in the GameGrid
 *
 * @author gp
 */
public class Block extends Cell
{
	/**
	 * img is a String that contains the path of the image Block
	 */
	private static final String img = "block.png";

	/**
	 * public constructor of block from a model-coordinate and a GameGrid
	 *
	 * @param coordinate the model-coordinate of the Cell
	 * @param gameGrid   the GameGrid to which the Cell is attached
	 */
	public Block(Vector2D coordinate, GameGrid gameGrid)
	{
		super(coordinate,
			FlightWeightImagesFactory.getInstance().getImage(img),
			gameGrid);
	}

	/**
	 * updateInModel() does nothing as the Blocks never change state
	 */
	@Override
	public void updateInModel()
	{
		// DO NOTHING
	}

	/**
	 * notifyExplosion() simply returns true as Blocks always block
	 * explosions
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return true as blocks always stop the explosions from propagating further
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return true; // Just handle it
	}

	/**
	 * allowsBombs() returns false as bombs must not be placed on top of
	 * Blocks
	 *
	 * @return false
	 */
	@Override
	public boolean allowsBombs()
	{
		return false;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
