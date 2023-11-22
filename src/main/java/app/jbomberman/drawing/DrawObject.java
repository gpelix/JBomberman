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
 
package app.jbomberman.drawing;

import app.jbomberman.image.AbstractImage;
import app.jbomberman.util.MutableVector2D;
import app.jbomberman.util.Vector2D;

import java.util.Objects;

/**
 * DrawingObject encapsulates an AbstractImage to draw
 *
 * @author gp
 * @see Drawing
 */
public class DrawObject extends MutableVector2D
{
	/**
	 * image is the AbstractImage of the DrawObject
	 */
	private final AbstractImage image;

	/**
	 * construct a DrawObject
	 *
	 * @param topLeftCorner the top left corner
	 * @param image         the image to be encapsulated
	 */
	public DrawObject(Vector2D topLeftCorner, AbstractImage image)
	{
		super(topLeftCorner);

		this.image = image;
	}

	/**
	 * getImage() gets the encapsulated image
	 *
	 * @return the incapsulated image
	 */
	public AbstractImage getImage()
	{
		return image;
	}

	/**
	 * getDimensions() gets the dimensions of the underlying image
	 *
	 * @return the dimensions of the underlying image
	 */
	public Vector2D getDimensions()
	{
		return image.getDimensions();
	}

	/**
	 * two DrawObjects are the same only if the images are the same and they
	 * must be placed in the same place
	 *
	 * @param obj the other object
	 * @return true if they obj and this are equal
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || obj.getClass() != getClass())
			return false;

		LayeredDrawObject other = (LayeredDrawObject) obj;

		return Objects.equals(this.getX(), other.getX()) && Objects.equals(getY(), other.getY()) && getImage() == other.getImage();
	}

	/**
	 * hashCode() must be overwritten to use Map and Set appropriately
	 *
	 * @return the hashCoe that is the combination of the hashCodes of the image
	 * and the coordinates
	 */
	@Override
	public int hashCode()
	{
		return Objects.hash(getX(), getY(), getImage());
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
