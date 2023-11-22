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
 
package app.jbomberman.image.javafx;

import app.jbomberman.JBomberManException;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.image.AbstractImageBuilder;
import app.jbomberman.util.Resources;
import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * JavaFXImageBuilder is the concrete implementation of AbstractImageBuilder used
 * with Javafx
 *
 * @author gp
 */
public class JavaFXImageBuilder extends AbstractImageBuilder
{
	/**
	 * @param path the path to the image in the images/ folder
	 * @return the JavaFXImage loaded
	 */
	@Override
	public JavaFXImage getImage(String path)
	{
		return new JavaFXImage(new Image(Resources.getResourceAsStream(path)));
	}

	/**
	 * getImageFromAbsolute() gets the image from an absolute path
	 *
	 * @param path the absolute path of the image
	 * @return the newly built AbstractImage
	 */
	@Override
	public AbstractImage getImageFromAbsolute(Path path)
	{
		try {
			return new JavaFXImage(new Image(Files.newInputStream(path)));
		} catch (IOException e) {
			throw new JBomberManException("Error while loading " + path);
		}
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
