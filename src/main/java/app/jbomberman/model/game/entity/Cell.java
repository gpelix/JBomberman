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
import app.jbomberman.image.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

/**
 * Cell is a class the models an Entity that does never change its state
 *
 * @author gp
 */
public abstract class Cell extends Entity
{
	/**
	 * img is the AbstractImage of the Cell
	 */
	private final AbstractImage img;

	/**
	 * protected constructor meant to be called by subclasses' constructors to
	 * initialize the Cell from its model-coordinate, the GameGrid to which it
	 * is attached and its AbstractImage
	 *
	 * @param coordinate the model-coordinate of the Cell
	 * @param gameGrid   the GameGrid to which the Cell is attached
	 * @param img        the image of the Cell
	 */
	protected Cell(Vector2D coordinate, AbstractImage img, GameGrid gameGrid)
	{
		super(coordinate, gameGrid);
		this.img = img;
	}

	/**
	 * getDrawing() simply gets the drawing with image img
	 *
	 * @return the Drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		return getDrawing(img);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/