package app.jbomberman.model.game;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Direction;

import java.util.List;

/**
 * FullMovementMovingImageManager extends the MovingImageManager allowing a
 * full 4-directions movement
 *
 * @author gp
 */
public class FullMovementMovingImageManager extends MovingImageManager
{
	/**
	 * left is the list of images to display when going westwards
	 */
	private final List<AbstractImage> left;
	/**
	 * right is the list of images to display when going eastwards
	 */
	private final List<AbstractImage> right;
	/**
	 * up is the list of images to display when going northwards
	 */
	private final List<AbstractImage> up;
	/**
	 * down is the list of images to display when going southwards
	 */
	private final List<AbstractImage> down;

	/**
	 * public constructor of FullMovementMovingImageManager from lists of images,
	 * one for every direction
	 *
	 * @param left  the list of images to use when going westwards
	 * @param right the list of images to use when going eastwards
	 * @param up    the list of images to use when going upwards
	 * @param down  the list of images to use when going downwards
	 */
	public FullMovementMovingImageManager(List<AbstractImage> left,
																				List<AbstractImage> right,
																				List<AbstractImage> up,
																				List<AbstractImage> down)
	{
		super(down, 0);

		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
	}

	/**
	 * getImageSet() must be used to get the set associated with the
	 * currentMoveDirection
	 *
	 * @param currentMoveDirection the direction of the movement
	 * @return the list of AbstractImage's associated with the
	 * currentMoveDirection
	 */
	@Override
	protected List<AbstractImage> getImageSet(Direction currentMoveDirection)
	{
		return switch (currentMoveDirection) {
			case EAST -> right;
			case WEST -> left;
			case NORTH -> up;
			case SOUTH -> down;
		};
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
