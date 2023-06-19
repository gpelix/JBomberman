package app.jbomberman.drawing;

import app.jbomberman.images.AbstractImage;
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
	 * @param image		 the image to be encapsulated
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


	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || obj.getClass() != getClass())
			return false;

		LayeredDrawObject other = (LayeredDrawObject) obj;

		return Objects.equals(this.getX(), other.getX()) && Objects.equals(getY(), other.getY()) && getImage() == other.getImage();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getX(), getY(), getImage());
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
