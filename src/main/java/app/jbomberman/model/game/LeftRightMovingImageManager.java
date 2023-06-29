package app.jbomberman.model.game;

import app.jbomberman.image.AbstractImage;
import app.jbomberman.util.Direction;

import java.util.List;

/**
 * LeftRightMovingImageManager is a MovingImageManager that only supports two
 * sets of image, one for left and one for right movement
 *
 * @author gp
 */
public class LeftRightMovingImageManager extends MovingImageManager
{
	/**
	 * left is the list of image that will be used when moving leftwards
	 */
	private final List<AbstractImage> left;
	/**
	 * right is the list of image that will be used when moving rightwards
	 */
	private final List<AbstractImage> right;

	/**
	 * public constructor of LeftRightMovingImageManager from a list of images
	 * for left movement and one for right movement
	 *
	 * @param left  the list of images to display when we are moving leftwards
	 * @param right the list of images to display when we are moving rightwards
	 */
	public LeftRightMovingImageManager(List<AbstractImage> left,
																		 List<AbstractImage> right)
	{
		super(left, 0);

		this.right = right;
		this.left = left;
	}

	/**
	 * getImageSet() must be used to get the set associated with the
	 * currentMoveDirection.
	 * Here we return the left set only if we are really going leftwards,
	 * otherwise we return the right set
	 *
	 * @param currentMoveDirection the direction of the movement
	 * @return the list of AbstractImage's associated with the
	 * currentMoveDirection
	 */
	@Override
	protected List<AbstractImage> getImageSet(Direction currentMoveDirection)
	{
		return switch (currentMoveDirection) {
			case NORTH, SOUTH, EAST -> right;
			case WEST -> left;
		};
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
