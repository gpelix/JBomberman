
package app.jbomberman.drawing;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.MutableVector2D;
import app.jbomberman.util.Vector2D;

public class DrawObject extends MutableVector2D
{
	private AbstractImage image;

	public DrawObject(Vector2D topLeftCorner, AbstractImage image)
	{
		super(topLeftCorner);

		this.image = image;
	}

	public AbstractImage getImage()
	{
		return image;
	}

	public Vector2D getDimensions()
	{
		return image.getDimensions();
	}
}
