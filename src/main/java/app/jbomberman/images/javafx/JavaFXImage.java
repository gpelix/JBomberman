package app.jbomberman.images.javafx;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Vector2D;
import javafx.scene.image.Image;

/**
 * JavaFXImage is the concrete implementation of AbstractImage that must be used
 * with javafx
 *
 * @author gp
 */
public class JavaFXImage extends AbstractImage
{
	/**
	 * the image that this image encapsulates
	 */
	private final Image image;

	/**
	 * build the JavaFXImage from a javafx.scene.image.Image
	 *
	 * @param image the image that must be encapsulated
	 */
	public JavaFXImage(Image image)
	{
		this.image = image;
	}

	/**
	 * unwrap() must be called to get the underlying image
	 *
	 * @return the underlying image
	 */
	public Image unwrap()
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
		return Vector2D.of((int) image.getWidth(), (int) image.getHeight());
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
