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
 
package app.jbomberman.view.widgets;

import app.jbomberman.drawing.Drawable;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.LayeredDrawObject;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.MutableVector2D;
import app.jbomberman.util.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Text represents an array of characters one beside the other.
 * It provides an easy way to create renderable text to be displayed
 * on the screen during the different scenes of the game
 *
 * @author gp
 */
public class Text extends MutableVector2D implements Drawable
{
	/**
	 * TextColor is the color of the text
	 *
	 * @author gp
	 */
	public enum TextColor
	{
		/**
		 * Black represents the black color
		 */
		Black,
		/**
		 * Gray is the enum representing the gray color
		 */
		Gray
	}

	/**
	 * text is a string representing the text currently displayed
	 */
	private String text;

	/**
	 * color represents the color of the text
	 */
	private TextColor color;

	/**
	 * images is the list of images that make up the text
	 */
	private List<AbstractImage> images;

	private Maybe<AbstractImage> icon = Maybe.nothing();

	/**
	 * drawing is the current drawing associated to the list of images
	 */
	private Drawing drawing;

	/**
	 * Text constructor from string, color, and position
	 *
	 * @param str                String of text
	 * @param color              the color of the text
	 * @param coordinatePosition a coordinate. The interpretation of that coordinate
	 *                           is up to the program in question
	 */
	public Text(String str, TextColor color, Vector2D coordinatePosition)
	{
		super(coordinatePosition);
		this.color = color;
		setText(str);
	}

	/**
	 * Text constructor from string, color and default zero position
	 *
	 * @param str   string of text
	 * @param color the color of the text
	 */
	public Text(String str, TextColor color)
	{
		this(str, color, Vector2D.getIdentity());
	}

	/**
	 * Text constructor setting empty string, color anda position
	 *
	 * @param color              the color of the text
	 * @param coordinatePosition the coordinate of the text
	 */
	public Text(TextColor color, Vector2D coordinatePosition)
	{
		this("", color, coordinatePosition);
	}

	/**
	 * setIcon() must be used to set an icon to the right of the Text
	 *
	 * @param icon the AbstractImage to set as icon
	 */
	public void setIcon(AbstractImage icon)
	{
		this.icon = Maybe.just(icon);
		updateDrawing();
	}

	/**
	 * unsetIcon() must be used to remove the icon
	 */
	public void unsetIcon()
	{
		this.icon = Maybe.nothing();
		updateDrawing();
	}

	/**
	 * setText() sets the text
	 *
	 * @param text string to set
	 */
	public void setText(String text)
	{
		this.text = text.toLowerCase(); // We only support lowercase letters
		updateImagesAndDrawings();
	}

	/**
	 * append() appends a string to the end of the current text
	 *
	 * @param text the text to append
	 */
	public void append(String text)
	{
		setText(this.text + text);
	}

	/**
	 * pop() pops a character from the end of the current text
	 */
	public void pop()
	{
		setText(text.substring(0, text.length() - 1));
	}

	/**
	 * setColor() is a method to set the color of the text
	 *
	 * @param color color to set
	 */
	public void setColor(TextColor color)
	{
		if (color != this.color) {
			this.color = color;
			updateImagesAndDrawings();
		}
	}

	/**
	 * updateImagesAndDrawings() is used to update both the images and the drawings
	 */
	private void updateImagesAndDrawings()
	{
		updateImages();
		updateDrawing();
	}

	/**
	 * updateImages() reloads all the images that represent the text
	 */
	private void updateImages()
	{
		FlightWeightImagesFactory factory = FlightWeightImagesFactory.getInstance();

		images = IntStream.range(0, text.length())
			.boxed()
			.map(i -> factory.getCharacterImage(text.charAt(i), color))
			.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * updateDrawing() updates the drawing and must be usually called after having
	 * updated the images
	 */
	private void updateDrawing()
	{
		int last = icon.isJust() ? length() + 1 : length();
		// This is the center of the text
		drawing = new Drawing(IntStream.range(0, last).boxed()
			.map(index -> {
					boolean isSpecial = index == length();
					return new LayeredDrawObject(
						this.sum(Vector2D.of((-last / 2 + index) * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL, isSpecial ? -FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL / 2 : 0)),
						isSpecial ? icon.fromJust() : images.get(index), 0);
				}
			));
		drawing.setDimensions(Vector2D.of((last / 2) * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL, FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL));
	}

	/**
	 * setPosition() sets the new position and regenerates the drawing
	 *
	 * @param coordinatePosition the coordinate to which the menu will be moved
	 */
	public void setPosition(Vector2D coordinatePosition)
	{
		getValuesFrom(coordinatePosition);
		updateDrawing();
	}

	/**
	 * of length of text
	 *
	 * @return the length of text
	 */
	public int length()
	{
		return text.length();
	}

	/**
	 * getDrawing() is used to get the drawing
	 *
	 * @return the drawing representing describing the text
	 */
	@Override
	public Drawing getDrawing()
	{
		return drawing;
	}

	/**
	 * getText() gets the text
	 *
	 * @return the text string
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * utility method used for debugging
	 *
	 * @return a string representing the text
	 */
	@Override
	public String toString()
	{
		return "Text[text=" + text + ", place=" + super.toString() + "]";
	}

	/**
	 * getColor() gets the current color of the text
	 *
	 * @return the color of the text
	 */
	public TextColor getColor()
	{
		return color;
	}

}

/* vim: set ts=2 sw=2 tw=0 noet :*/
