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
 
package app.jbomberman.model.game;

import app.jbomberman.image.AbstractImage;
import app.jbomberman.util.Direction;

import java.util.List;

/**
 * FullMovementMovingImageManager extends the MovingImageManager allowing a
 * full 4-directions movement
 *
 * @author gp
 */
public class FullMovementMovingImageManager extends MovingImageManager
{
	/**
	 * left is the list of images to display when going westwards
	 */
	private final List<AbstractImage> left;
	/**
	 * right is the list of images to display when going eastwards
	 */
	private final List<AbstractImage> right;
	/**
	 * up is the list of images to display when going northwards
	 */
	private final List<AbstractImage> up;
	/**
	 * down is the list of images to display when going southwards
	 */
	private final List<AbstractImage> down;

	/**
	 * public constructor of FullMovementMovingImageManager from lists of images,
	 * one for every direction
	 *
	 * @param left  the list of images to use when going westwards
	 * @param right the list of images to use when going eastwards
	 * @param up    the list of images to use when going upwards
	 * @param down  the list of images to use when going downwards
	 */
	public FullMovementMovingImageManager(List<AbstractImage> left,
																				List<AbstractImage> right,
																				List<AbstractImage> up,
																				List<AbstractImage> down)
	{
		super(down, 0);

		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
	}

	/**
	 * getImageSet() must be used to get the set associated with the
	 * currentMoveDirection
	 *
	 * @param currentMoveDirection the direction of the movement
	 * @return the list of AbstractImage's associated with the
	 * currentMoveDirection
	 */
	@Override
	protected List<AbstractImage> getImageSet(Direction currentMoveDirection)
	{
		return switch (currentMoveDirection) {
			case EAST -> right;
			case WEST -> left;
			case NORTH -> up;
			case SOUTH -> down;
		};
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
