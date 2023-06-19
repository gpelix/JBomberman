package app.jbomberman.images;

import app.jbomberman.util.Vector2D;

/**
 * AbstractImage is an abstractImage. It only defines a common way to getDimensions
 * but nothing else.
 *
 * @author gp
 */
public abstract class AbstractImage
{
	/**
	 * getDimensions() is used to get the dimensions of the AbstractImage
	 *
	 * @return the dimensions of the image
	 */
	public abstract Vector2D getDimensions();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
