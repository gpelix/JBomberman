package app.jbomberman.drawing;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.FlightWeightImagesFactory;
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
	public enum TextColor
	{
		Black, Gray
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

	/**
	 * drawing is the current drawing associated to the list of images
	 */
	private Drawing drawing;

	/**
	 * Text constructor from string, color, and position
	 *
	 * @param str				String of text
	 * @param color			  the color of the text
	 * @param coordinatePosition a coordinate. The interpretation of that coordinate
	 *						   is up to the program in question
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
	 * @param color			  the color of the text
	 * @param coordinatePosition the coordinate of the text
	 */
	public Text(TextColor color, Vector2D coordinatePosition)
	{
		this("", color, coordinatePosition);
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
		// This is the center of the text
		drawing = new Drawing(IntStream.range(0, length()).boxed()
			.map(index -> new LayeredDrawObject(
				this.sum(Vector2D.of((-length() / 2 + index) * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL, 0)),
				images.get(index), 0)
			));
		drawing.setDimensions(Vector2D.of((length() / 2) * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL, FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL));
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
