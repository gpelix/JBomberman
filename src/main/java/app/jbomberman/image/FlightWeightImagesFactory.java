package app.jbomberman.image;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.util.Maybe;
import app.jbomberman.view.widgets.Text;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FlightWeightImagesFactory is a class that can handle Image loading. The idea
 * behind this is that loading a lot of images takes a lot of time, takes a
 * lot of memory if there are many duplicates and are not handled properly.
 * The singleton pattern is used as we want to load every image through the
 * same factory in order to avoid loading two times the same image due to the
 * use of different factories. Of course, we are also using the flightweight
 * pattern
 *
 * @author gp
 */
public class FlightWeightImagesFactory
{
	/**
	 * factory is the unique instance of FlightWeightImagesFactory that is
	 * created in the program
	 */
	static private FlightWeightImagesFactory factory;

	/**
	 * CHARACTER_SIZE_IN_PIXEL is a static int that specifies the size in
	 * pixel of the square images containing characters
	 */
	static public final int CHARACTER_SIZE_IN_PIXEL = 8 * Properties.getInstance().getScaleFactor();

	/**
	 * of the unique instance of FlightWeightImagesFactory
	 *
	 * @return instance of FlightWeightImagesFactory
	 */
	public static FlightWeightImagesFactory getInstance()
	{
		if (factory == null)
			factory = new FlightWeightImagesFactory();
		return factory;
	}

	/**
	 * imageBuilder is a reference to the concrete imageBuilder used to create
	 * images. At the moment it's a JavaFXImageBuilder
	 */
	private static Maybe<AbstractImageBuilder> imageBuilder = Maybe.nothing();

	/**
	 * initializeImageBuilder must be called at an early stage during the
	 * setup process to initialize the Builder of Images
	 *
	 * @param imageBuilder the AbstractImageBuilder that will build images
	 */
	public static void initializeImageBuilder(AbstractImageBuilder imageBuilder)
	{
		FlightWeightImagesFactory.imageBuilder = Maybe.just(imageBuilder);
	}

	/**
	 * We use a Map to implement a mapping between path names to images for
	 * fast retrieval
	 */
	private final Map<String, AbstractImage> images;

	/**
	 * private constructor for FlightWeightImagesFactory that must only be
	 * called from the getInstance() function
	 */
	private FlightWeightImagesFactory()
	{
		images = new ConcurrentHashMap<>();
	}

	/**
	 * of an image either loading it from file or if it has already been
	 * loaded, of it from the inner map
	 *
	 * @param path path of the resource that contains the image to of
	 * @return the loaded image
	 */
	public AbstractImage getImage(String path)
	{
		return images.computeIfAbsent(path,
			p -> imageBuilder.unwrapOrThrow(() -> new JBomberManException(
				"FlightWeightImagesFactory must be initialized before its first use calling initializeImageBuilder")
			).getImage(Properties.getInstance().getImagesDirectory() + p)
		);
	}

	/**
	 * getCharacterImage is similar to getImage, but we specify a char instead
	 * of a filename. This function loads the image associated with the
	 * character.
	 *
	 * @param c     character whose image we want to load
	 * @param color the color of the images
	 * @return the loaded image of the character
	 */
	public AbstractImage getCharacterImage(char c, Text.TextColor color)
	{
		return getImage(switch (c) {
			case '.' -> "dot";
			case ' ' -> "space";
			case ':' -> "column";
			case '/' -> "slash";
			default -> String.valueOf(c);
		} + (color == Text.TextColor.Black ? "" : "_gray") + ".png");
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
