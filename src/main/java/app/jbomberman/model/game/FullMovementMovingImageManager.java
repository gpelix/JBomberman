
package app.jbomberman.model.game;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Direction;

import java.util.List;

public class FullMovementMovingImageManager extends MovingImageManager
{
	private final List<AbstractImage> left;
	private final List<AbstractImage> right;
	private final List<AbstractImage> up;
	private final List<AbstractImage> down;

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
