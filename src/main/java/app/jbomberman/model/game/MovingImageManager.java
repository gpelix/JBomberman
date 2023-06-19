package app.jbomberman.model.game;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;

import java.util.List;

public abstract class MovingImageManager extends ImageManager
{
	private Maybe<Direction> previousMoveDirection = Maybe.nothing();

	protected MovingImageManager(List<AbstractImage> currentImages, int idxInImageViews)
	{
		super(currentImages, idxInImageViews);
	}

	public AbstractImage getNextImage(Maybe<Direction> currentMoveDirection)
	{
		if (currentMoveDirection.isNothing())
			return getCurrentImage();

		Direction currentMoveDirectionU = currentMoveDirection.fromJust();

		if (previousMoveDirection.compute(true,
			x -> !x.equals(currentMoveDirectionU)))
		{
			setIdxInImageViews(0);
			previousMoveDirection = currentMoveDirection;
			setCurrentImages(getImageSet(currentMoveDirectionU));
		}

		return super.getNextImage();
	}

	protected abstract List<AbstractImage> getImageSet(Direction currentMoveDirection);
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
