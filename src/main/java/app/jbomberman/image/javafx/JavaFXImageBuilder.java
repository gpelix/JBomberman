package app.jbomberman.image.javafx;

import app.jbomberman.image.AbstractImageBuilder;
import app.jbomberman.util.Resources;
import javafx.scene.image.Image;

/**
 * JavaFXImageBuilder is the concrete implementation of AbstractImageBuilder used
 * with Javafx
 *
 * @author gp
 */
public class JavaFXImageBuilder extends AbstractImageBuilder
{
	/**
	 * @param path the path to the image in the images/ folder
	 * @return the JavaFXImage loaded
	 */
	@Override
	public JavaFXImage getImage(String path)
	{
		return new JavaFXImage(new Image(Resources.getResourceAsStream(path)));
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
