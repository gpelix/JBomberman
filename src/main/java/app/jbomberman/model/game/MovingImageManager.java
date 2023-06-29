package app.jbomberman.model.game;

import app.jbomberman.image.AbstractImage;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;

import java.util.List;

/**
 * MovingImageManager is an ImageManager that supports multiple sets of images,
 * and the right image will be chosen depending on the direction specified
 *
 * @author gp
 */
public abstract class MovingImageManager extends ImageManager
{
	/**
	 * previousMoveDirection is a Maybe that contains the previous direction if
	 * any.
	 * The previous direction must be known by getImage() to choose if we must
	 * rewind the set of images
	 */
	private Maybe<Direction> previousMoveDirection = Maybe.nothing();


	/**
	 * public constructor of MovingImageManager from a list of Images
	 *
	 * @param images      the list of images
	 * @param idxInImages the index in the image list of the current image
	 */
	protected MovingImageManager(List<AbstractImage> images, int idxInImages)
	{
		super(images, idxInImages);
	}

	/**
	 * getImage() gets the current image after having updated it
	 *
	 * @param currentMoveDirection the direction movement
	 * @return the current AbstractImage after update
	 */
	public AbstractImage getImage(Maybe<Direction> currentMoveDirection)
	{
		if (currentMoveDirection.isNothing())
			return getCurrentImage();

		Direction currentMoveDirectionU = currentMoveDirection.fromJust();

		if (previousMoveDirection.compute(true,
			x -> !x.equals(currentMoveDirectionU)))
		{
			setIdxInImages(0);
			previousMoveDirection = currentMoveDirection;
			setImages(getImageSet(currentMoveDirectionU));
		}

		return super.getImage();
	}

	/**
	 * getImageSet() must be used to get the set associated with the
	 * currentMoveDirection
	 *
	 * @param currentMoveDirection the direction of the movement
	 * @return the list of AbstractImage's associated with the
	 * currentMoveDirection
	 */
	protected abstract List<AbstractImage> getImageSet(Direction currentMoveDirection);
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
