package app.jbomberman.images.swing;

import app.jbomberman.images.AbstractImage;
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
