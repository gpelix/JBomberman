
package app.jbomberman.util;

import app.jbomberman.controller.KeyBoardController;

public enum Direction
{
	NORTH(Vector2D.of(0, -1)),
	EAST(Vector2D.of(1, 0)),
	SOUTH(Vector2D.of(0, 1)),
	WEST(Vector2D.of(-1, 0));

	private final Vector2D associatedCoordinate;

	Direction(Vector2D associatedCoordinate)
	{
		this.associatedCoordinate = associatedCoordinate;
	}

	public Vector2D getAssociatedCoordinate()
	{
		return associatedCoordinate;
	}

	public boolean isHorizontal()
	{
		return this == EAST || this == WEST;
	}

	public boolean isVertical()
	{
		return this == NORTH || this == SOUTH;
	}

	public static Maybe<Direction> fromKeyCode(KeyBoardController.KeyBoardControllerKeyCode keyCode)
	{
		return switch (keyCode) {
			case UP -> Maybe.just(NORTH);
			case DOWN -> Maybe.just(SOUTH);
			case LEFT -> Maybe.just(WEST);
			case RIGHT -> Maybe.just(EAST);
			default -> Maybe.nothing();
		};
	}

	public static Maybe<Direction> fromDirectionCoordinate(Vector2D coordinate)
	{
		int x = coordinate.getX();
		int y = coordinate.getY();

		return Maybe.something(
			x > 0 ? EAST : x < 0 ? WEST : y > 0 ? SOUTH : y < 0 ? NORTH : null);
	}

	public Vector2D asVector2D()
	{
		return switch (this) {
			case EAST -> Vector2D.of(1, 0);
			case WEST -> Vector2D.of(-1, 0);
			case SOUTH -> Vector2D.of(0, 1);
			case NORTH -> Vector2D.of(0, -1);
		};
	}

	public Direction opposite()
	{
		return switch (this) {
			case EAST -> WEST;
			case WEST -> EAST;
			case NORTH -> SOUTH;
			case SOUTH -> NORTH;
		};
	}
}
