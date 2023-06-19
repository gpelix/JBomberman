package app.jbomberman.drawing;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Vector2D;

// TODO: javadoc
public class LayeredDrawObject extends DrawObject
{
	private int layer;

	/**
	 * construct a DrawObject
	 *
	 * @param topLeftCorner the top left corner
	 * @param image		 the image to be encapsulated
	 */
	public LayeredDrawObject(Vector2D topLeftCorner, AbstractImage image, int layer)
	{
		super(topLeftCorner, image);

		this.layer = layer;
	}

	public int getLayer()
	{
		return layer;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
