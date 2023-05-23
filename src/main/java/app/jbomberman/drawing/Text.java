
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
public class Text extends MutableVector2D
{
	/**
	 * text is a string representing the text currently displayed
	 */
	private String text;

	/**
	 * black is a boolean value that says if the background color of the
	 * letters is black or not (gray otherwise)
	 */
	private boolean black;

	private List<AbstractImage> images;

	private Drawing drawing;

	/**
	 * Text constructor
	 *
	 * @param str   String of text
	 * @param black boolean that says if the background color of the
	 *              letters is black or not (gray otherwise)
	 */
	public Text(String str, boolean black, Vector2D modelCoordinatePosition)
	{
		super(modelCoordinatePosition);
		this.black = black;
		setText(str);
	}

	public Text(String str, boolean black)
	{
		this(str, black, Vector2D.of(0, 0));
	}

	public Text(boolean b, Vector2D modelCoordinatePosition)
	{
		this("", b, modelCoordinatePosition);
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

	public void append(String text)
	{
		setText(this.text + text);
	}

	public void pop()
	{
		setText(this.text.substring(0, text.length() - 1));
	}

	/**
	 * setColor() is a method to set the color of the text
	 *
	 * @param black color to set
	 */
	public void setColor(boolean black)
	{
		this.black = black;
		updateImagesAndDrawings();
	}

	private void updateImagesAndDrawings()
	{
		updateImages();
		updateDrawings();
	}

	private void updateImages()
	{
		FlightWeightImagesFactory factory = FlightWeightImagesFactory.getInstance();

		images = IntStream.range(0, text.length())
			.boxed()
			.map(i -> factory.getCharacterImage(text.charAt(i), black))
			.collect(Collectors.toCollection(ArrayList::new));
	}

	private void updateDrawings()
	{
		// This is the center of the text
		drawing = new Drawing(IntStream.range(0, length()).boxed()
			.map(index -> new DrawObject(this.sum(Vector2D.of((-length() / 2 + index) * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL, 0)),
				images.get(index))
			));
		drawing.setDimensions(Vector2D.of((length() / 2) * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL, FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL));
	}

	public void setPosition(Vector2D modelCoordinatePosition)
	{
		getValuesFrom(modelCoordinatePosition);
		updateDrawings();
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

	public int spanLength()
	{
		return length() * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL;
	}

	public Drawing getDrawing()
	{
		return drawing;
	}

	public String getText()
	{
		return text;
	}

	@Override
	public String toString()
	{
		return "Text[text=" + text + ", place=" + super.toString() + "]";
	}

	public boolean getColor()
	{
		return black;
	}

}
