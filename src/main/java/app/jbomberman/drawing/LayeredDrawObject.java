package app.jbomberman.drawing;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Vector2D;

/**
 * LayeredDrawObject is a DrawObject with an additional field that specifies the
 * layer to which the object must be placed.
 *
 * @author gp
 */
public class LayeredDrawObject extends DrawObject
{
	/**
	 * the layer containing the object. This number can be interpreted differently
	 * in different applications
	 */
	private int layer;

	/**
	 * construct a DrawObject
	 *
	 * @param topLeftCorner the top left corner
	 * @param image         the image to be encapsulated
	 * @param layer         the layer of LayeredDrawObject
	 */
	public LayeredDrawObject(Vector2D topLeftCorner, AbstractImage image, int layer)
	{
		super(topLeftCorner, image);

		this.layer = layer;
	}

	/**
	 * getLayer() gets the layer of the LayeredObject
	 *
	 * @return the layer of the LayeredDrawObject
	 */
	public int getLayer()
	{
		return layer;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
