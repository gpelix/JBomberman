package app.jbomberman.image.swing;

import app.jbomberman.JBomberManException;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.image.AbstractImageBuilder;
import app.jbomberman.util.Resources;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * SwingImageBuilder is the concrete implementation of AbstractImageBuilder used
 * with Java Swing
 *
 * @author gp
 */
public class SwingImageBuilder extends AbstractImageBuilder
{
	/**
	 * @param path the path to the image in the images/ folder
	 * @return the SwingImage loaded
	 */
	@Override
	public SwingImage getImage(String path)
	{
		try {
			return new SwingImage(ImageIO.read(Resources.getResourceAsStream(path)));
		} catch (IOException e) {
			throw new JBomberManException("Unable to load " + path);
		}
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
			return new SwingImage(ImageIO.read(Files.newInputStream(path)));
		} catch (IOException e) {
			throw new JBomberManException("Unable to load " + path);
		}
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
