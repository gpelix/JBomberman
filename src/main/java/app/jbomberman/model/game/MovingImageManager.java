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
import app.jbomberman.util.Maybe;

import java.util.List;

/**
 * MovingImageManager is an ImageManager that supports multiple sets of images,
 * and the right image will be chosen depending on the direction specified
 *
 * @author gp
 */
public abstract class MovingImageManager extends ImageManager
{
	/**
	 * previousMoveDirection is a Maybe that contains the previous direction if
	 * any.
	 * The previous direction must be known by getImage() to choose if we must
	 * rewind the set of images
	 */
	private Maybe<Direction> previousMoveDirection = Maybe.nothing();


	/**
	 * public constructor of MovingImageManager from a list of Images
	 *
	 * @param images      the list of images
	 * @param idxInImages the index in the image list of the current image
	 */
	protected MovingImageManager(List<AbstractImage> images, int idxInImages)
	{
		super(images, idxInImages);
	}

	/**
	 * getImage() gets the current image after having updated it
	 *
	 * @param currentMoveDirection the direction movement
	 * @return the current AbstractImage after update
	 */
	public AbstractImage getImage(Maybe<Direction> currentMoveDirection)
	{
		if (currentMoveDirection.isNothing())
			return getCurrentImage();

		Direction currentMoveDirectionU = currentMoveDirection.fromJust();

		if (previousMoveDirection.compute(true,
			x -> !x.equals(currentMoveDirectionU)))
		{
			setIdxInImages(0);
			previousMoveDirection = currentMoveDirection;
			setImages(getImageSet(currentMoveDirectionU));
		}

		return super.getImage();
	}

	/**
	 * getImageSet() must be used to get the set associated with the
	 * currentMoveDirection
	 *
	 * @param currentMoveDirection the direction of the movement
	 * @return the list of AbstractImage's associated with the
	 * currentMoveDirection
	 */
	protected abstract List<AbstractImage> getImageSet(Direction currentMoveDirection);
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
