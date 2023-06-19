package app.jbomberman.model.game;

import app.jbomberman.model.game.entity.Block;
import app.jbomberman.model.game.entity.Bomb;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.GameCharacterCapabilities;
import app.jbomberman.model.game.entity.Powerup;
import app.jbomberman.model.game.entity.Wall;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.stream.Stream;

public class MoveManager
{
	private final GameGrid gameGrid;

	public MoveManager(GameGrid gameGrid)
	{
		this.gameGrid = gameGrid;
	}

	public Vector2D getTopRight(Vector2D coordinate)
	{
		return Vector2D.of(coordinate.getX() + Entity.SIZE_IN_MODEL - 1,
			coordinate.getY());
	}

	public Vector2D getBottomRight(Vector2D coordinate)
	{
		return Vector2D.of(coordinate.getX() + Entity.SIZE_IN_MODEL - 1,
			coordinate.getY() + Entity.SIZE_IN_MODEL - 1);
	}

	public Vector2D getBottomLeft(Vector2D coordinate)
	{
		return Vector2D.of(coordinate.getX(),
			coordinate.getY() + Entity.SIZE_IN_MODEL - 1);
	}

	/**
	 * @param finalPlaceTopLeft gameCharacter's top left coordinate where it
	 *						  wants to go
	 * @return a boolean that says if the gameCharacter can be moved to (x, y) or not
	 */
	public boolean canAccessForCommonSizeEntity(GameCharacterCapabilities capabilities,
												Vector2D finalPlaceTopLeft,
												Vector2D currentEntityGameGridCoordinate,
												Maybe<Direction> moveDirection)
	{
		return generalCanAccess(Stream.of(getBottomRight(finalPlaceTopLeft),
				getTopRight(finalPlaceTopLeft),
				finalPlaceTopLeft, getBottomLeft(finalPlaceTopLeft)),
			capabilities, currentEntityGameGridCoordinate, moveDirection);
	}

	public boolean canAccessForSmallEntity(BomberManCapabilities capabilities,
										   Vector2D finalPlaceTopLeft,
										   Vector2D currentEntityGameGridCoordinate,
										   Direction moveDirection)
	{
		Stream<Vector2D> coordinates = moveDirection.isHorizontal() ?
			Stream.of(
				getBottomLeft(finalPlaceTopLeft).sum(Vector2D.of(0, -PIXEL_OFFSET)),
				getBottomRight(finalPlaceTopLeft).sum(Vector2D.of(0, -PIXEL_OFFSET)),
				finalPlaceTopLeft.sum(Vector2D.of(0, PIXEL_OFFSET)),
				getTopRight(finalPlaceTopLeft).sum(Vector2D.of(0, PIXEL_OFFSET))
			) : Stream.of(
			getBottomLeft(finalPlaceTopLeft).sum(Vector2D.of(PIXEL_OFFSET, 0)),
			getBottomRight(finalPlaceTopLeft).sum(Vector2D.of(-PIXEL_OFFSET, 0)),
			finalPlaceTopLeft.sum(Vector2D.of(PIXEL_OFFSET, 0)),
			getTopRight(finalPlaceTopLeft).sum(Vector2D.of(-PIXEL_OFFSET, 0))
		);

		return generalCanAccess(
			coordinates,
			capabilities, currentEntityGameGridCoordinate,
			Maybe.just(moveDirection));
	}

	private boolean generalCanAccess(Stream<Vector2D> coordinatesToCheck,
									 GameCharacterCapabilities capabilities,
									 Vector2D currentEntityGameGridCoordinate,
									 Maybe<Direction> moveDirection)
	{
		return coordinatesToCheck.allMatch(c ->
			checkCoordinate(c, capabilities, currentEntityGameGridCoordinate, moveDirection));
	}

	public boolean checkCoordinate(Vector2D coordinate,
								   GameCharacterCapabilities capabilities,
								   Vector2D currentEntityGameGridCoordinate,
								   Maybe<Direction> moveDirection)
	{
		Vector2D gridCoordinate = gameGrid.modelCoord2GridCoord(coordinate);

		if (isOnAliveWall(gridCoordinate) && !capabilities.goesThroughWalls())
			return false;

		if (isOnBlock(gridCoordinate))
			return false;

		if (isOnPowerUp(gridCoordinate) && !capabilities.takesPowerUps())
			return false;

		if (!(capabilities instanceof BomberManCapabilities bomberManCapabilities &&
			bomberManCapabilities.goesThroughBombs()))
		{
			Maybe<Direction> directionTowardsBomb =
				Direction.fromDirectionVector(gridCoordinate.sub(currentEntityGameGridCoordinate));

			if (directionTowardsBomb.isJust() && moveDirection.isJust() &&
				directionTowardsBomb.fromJust().equals(moveDirection.fromJust()) &&
				isOnBomb(gridCoordinate) && !isOnBomb(currentEntityGameGridCoordinate))
				return false;
		}

		return true;
	}

	private static final int PIXEL_OFFSET = 15;


	public boolean isOnBlock(Vector2D gridCoordinate)
	{
		return gameGrid.get(gridCoordinate)
			.getEntitiesOn()
			.stream()
			.anyMatch(entity -> entity instanceof Block);
	}

	public boolean isOnBomb(Vector2D gridCoordinate)
	{
		return gameGrid.get(gridCoordinate)
			.getEntitiesOn()
			.stream()
			.anyMatch(entity -> entity instanceof Bomb);
	}

	public boolean isOnAliveWall(Vector2D gridCoordinate)
	{
		return gameGrid.get(gridCoordinate)
			.getEntitiesOn()
			.stream()
			.anyMatch(entity -> entity instanceof Wall && !((Wall) entity).isDisappearing());
	}

	private boolean isOnPowerUp(Vector2D gridCoordinate)
	{
		return gameGrid.get(gridCoordinate)
			.getEntitiesOn()
			.stream()
			.anyMatch(entity -> entity instanceof Powerup);
	}

	/**
	 * @param topLeft topLeftCoordinate of a GameCharacter
	 * @return true if the bomberManCharacter could move relatively to blocks
	 * in at least 3 directions
	 */
	public boolean isAtCrossroad(Vector2D topLeft)
	{
		Vector2D normalizedCoordinate =
			topLeft.sub(gameGrid.getTopLeftCornerModelCoordinate());

		// Check if we are aligned with the rows and columns in the grid
		if (!normalizedCoordinate.mod(Entity.SIZE_IN_MODEL).isZero())
			return false;

		Vector2D gridCoordinate = gameGrid.modelCoord2GridCoord(topLeft);

		// Check that we are in odd rows and columns
		return gridCoordinate.getY() % 2 == 1 && gridCoordinate.getX() % 2 == 1;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
