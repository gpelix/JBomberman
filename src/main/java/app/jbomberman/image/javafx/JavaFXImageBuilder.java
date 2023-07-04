package app.jbomberman.image.javafx;

import app.jbomberman.JBomberManException;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.image.AbstractImageBuilder;
import app.jbomberman.util.Resources;
import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

	/**
	 * getImageFromAbsolute() gets the image from an absolute path
	 *
	 * @param path the absolute path of the image
	 * @return the newly built AbstractImage
	 */
	@Override
	public AbstractImage getImageFromAbsolute(Path path)
	{
		try {
			return new JavaFXImage(new Image(Files.newInputStream(path)));
		} catch (IOException e) {
			throw new JBomberManException("Error while loading " + path);
		}
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
