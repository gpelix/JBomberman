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
 
package app.jbomberman.image.swing;

import app.jbomberman.image.AbstractImage;
import app.jbomberman.util.Vector2D;

import java.awt.image.BufferedImage;

/**
 * SwingImage is the concrete implementation of the AbstractImage used with
 * the swing library
 *
 * @author gp
 */
public class SwingImage extends AbstractImage
{
	/**
	 * the image that this image encapsulates
	 */
	private final BufferedImage image;

	/**
	 * build the SwingImage from a BufferedImage
	 *
	 * @param image the BufferedImage that must be encapsulated
	 */
	public SwingImage(BufferedImage image)
	{
		this.image = image;
	}

	/**
	 * unwrap() must be called to get the underlying image
	 *
	 * @return the underlying image
	 */
	public BufferedImage unwrap()
	{
		return image;
	}

	/**
	 * getDimensions() gets the dimensions of the image
	 *
	 * @return the dimensions of the image
	 */
	@Override
	public Vector2D getDimensions()
	{
		return Vector2D.of(image.getWidth(null), image.getHeight(null));
	}

}

/* vim: set ts=2 sw=2 tw=0 noet :*/
