package app.jbomberman.model.game;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Direction;

import java.util.List;

public class LeftRightMovingImageManager extends MovingImageManager
{
	private final List<AbstractImage> left;
	private final List<AbstractImage> right;

	public LeftRightMovingImageManager(List<AbstractImage> left,
									   List<AbstractImage> right)
	{
		super(left, 0);

		this.right = right;
		this.left = left;
	}

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
