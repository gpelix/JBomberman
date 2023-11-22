/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
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

/**
 * MoveManager is the class responsible for the movement of Entities on the
 * GameGrid
 *
 * @author gp
 */
public class MoveManager
{
	/**
	 * GameGrid is the gameGrid to which the MoveManager is associated
	 */
	private final GameGrid gameGrid;

	/**
	 * public constructor of the MoveManager from the GameGrid
	 *
	 * @param gameGrid the gameGrid to which the MoveManager must be associated
	 */
	public MoveManager(GameGrid gameGrid)
	{
		this.gameGrid = gameGrid;
	}

	/**
	 * getTopRight() returns the topRight coordinate of from a simple
	 * ModelCoordinate of topLeft
	 *
	 * @param coordinate the topLeft coordinate of the Entity
	 * @return the topRightCoordinate of the Entity
	 */
	public Vector2D getTopRight(Vector2D coordinate)
	{
		return Vector2D.of(coordinate.getX() + Entity.SIZE_IN_MODEL - 1,
			coordinate.getY());
	}

	/**
	 * getBottomRight() returns the bottomRight coordinate of from a simple
	 * ModelCoordinate of topLeft
	 *
	 * @param coordinate the topLeft coordinate of the Entity
	 * @return the BottomRightCoordinate of the Entity
	 */
	public Vector2D getBottomRight(Vector2D coordinate)
	{
		return Vector2D.of(coordinate.getX() + Entity.SIZE_IN_MODEL - 1,
			coordinate.getY() + Entity.SIZE_IN_MODEL - 1);
	}

	/**
	 * getBottomLeft() returns the bottomLeft coordinate of from a simple
	 * ModelCoordinate of topLeft
	 *
	 * @param coordinate the topLeft coordinate of the Entity
	 * @return the bottomLeftCoordinate of the Entity
	 */
	public Vector2D getBottomLeft(Vector2D coordinate)
	{
		return Vector2D.of(coordinate.getX(),
			coordinate.getY() + Entity.SIZE_IN_MODEL - 1);
	}

	/**
	 * canAccessForCommonSizeEntity() must be used to check if the move is valid
	 * for enemies and other automated moving entities
	 *
	 * @param capabilities                    the capabilities of the Entity
	 * @param finalPlaceTopLeft               final place to which bomberman wants
	 *                                        to move
	 * @param currentEntityGameGridCoordinate the current model coordinate of the entity
	 * @param moveDirection                   the direction of the movement
	 * @return true if and only if the move is valid
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

	/**
	 * canAccessForSmallEntity() must be called to check if a movement is ok for
	 * bomberman.
	 * <p>
	 * BomberMan needs more flexibility than Enemies. We don't want to be too strict, and
	 * so we make BomberMan's icon smaller to get more freedom
	 *
	 * @param capabilities                    the capabilities of the Entity
	 * @param finalPlaceTopLeft               final place to which bomberman wants
	 *                                        to move
	 * @param currentEntityGameGridCoordinate the current model coordinate of the entity
	 * @param moveDirection                   the direction of the movement
	 * @return true if and only if the move is valid
	 */
	public boolean canAccessForSmallEntity(BomberManCapabilities capabilities,
																				 Vector2D finalPlaceTopLeft,
																				 Vector2D currentEntityGameGridCoordinate,
																				 Direction moveDirection)
	{
		Stream<Vector2D> coordinates = moveDirection.isHorizontal() ?
			Stream.of(
				getBottomLeft(finalPlaceTopLeft).sum(Vector2D.of(0, -MODEL_SQUARE_OFFSET)),
				getBottomRight(finalPlaceTopLeft).sum(Vector2D.of(0, -MODEL_SQUARE_OFFSET)),
				finalPlaceTopLeft.sum(Vector2D.of(0, MODEL_SQUARE_OFFSET)),
				getTopRight(finalPlaceTopLeft).sum(Vector2D.of(0, MODEL_SQUARE_OFFSET))
			) : Stream.of(
			getBottomLeft(finalPlaceTopLeft).sum(Vector2D.of(MODEL_SQUARE_OFFSET, 0)),
			getBottomRight(finalPlaceTopLeft).sum(Vector2D.of(-MODEL_SQUARE_OFFSET, 0)),
			finalPlaceTopLeft.sum(Vector2D.of(MODEL_SQUARE_OFFSET, 0)),
			getTopRight(finalPlaceTopLeft).sum(Vector2D.of(-MODEL_SQUARE_OFFSET, 0))
		);

		return generalCanAccess(
			coordinates,
			capabilities, currentEntityGameGridCoordinate,
			Maybe.just(moveDirection));
	}

	/**
	 * generalCanAccess() must be called internally to check if a move is valid or
	 * not
	 *
	 * @param coordinatesToCheck              a Stream of coordinates that must be
	 *                                        checked
	 * @param capabilities                    the capabilities of the Entity
	 * @param currentEntityGameGridCoordinate the current model coordinate of the entity
	 * @param moveDirection                   the direction of the movement
	 * @return true if and only if the move is valid
	 */
	private boolean generalCanAccess(Stream<Vector2D> coordinatesToCheck,
																	 GameCharacterCapabilities capabilities,
																	 Vector2D currentEntityGameGridCoordinate,
																	 Maybe<Direction> moveDirection)
	{
		return coordinatesToCheck.allMatch(c ->
			checkCoordinate(c, capabilities, currentEntityGameGridCoordinate, moveDirection));
	}

	/**
	 * checkCoordinate() checks whether the entity with the
	 * GameCharacterCapabilities specified in capabilities can move to
	 * finalCoordinate model coordinate
	 *
	 * @param finalCoordinate                 the final coordinate to which the entity wants to
	 *                                        go
	 * @param capabilities                    the capabilities of the Entity
	 * @param currentEntityGameGridCoordinate the current model coordinate of the entity
	 * @param moveDirection                   the direction of the movement
	 * @return true if and only if the move is valid
	 */
	public boolean checkCoordinate(Vector2D finalCoordinate,
																 GameCharacterCapabilities capabilities,
																 Vector2D currentEntityGameGridCoordinate,
																 Maybe<Direction> moveDirection)
	{
		Vector2D gridCoordinate = gameGrid.modelCoord2GridCoord(finalCoordinate);

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

	/**
	 * MODEL_SQUARE_OFFSET is the size of a square in model coordinates
	 */
	private static final int MODEL_SQUARE_OFFSET = 15;

	/**
	 * isOnBlock() check whether the gridCoordinate is a FreeCell containing a
	 * Block
	 *
	 * @param gridCoordinate the gridCoordinate of the FreeCell to check
	 * @return true if and only if the FreeCell at gridCoordinate contains a Block
	 */
	public boolean isOnBlock(Vector2D gridCoordinate)
	{
		return gameGrid.get(gridCoordinate)
			.getEntitiesOn()
			.stream()
			.anyMatch(entity -> entity instanceof Block);
	}

	/**
	 * isOnBomb() check whether the gridCoordinate is a FreeCell containing a
	 * Bomb
	 *
	 * @param gridCoordinate the gridCoordinate of the FreeCell to check
	 * @return true if and only if the FreeCell at gridCoordinate contains a Bomb
	 */
	public boolean isOnBomb(Vector2D gridCoordinate)
	{
		return gameGrid.get(gridCoordinate)
			.getEntitiesOn()
			.stream()
			.anyMatch(entity -> entity instanceof Bomb);
	}

	/**
	 * isOnAliveWall() check whether the gridCoordinate is a FreeCell containing a
	 * Wall
	 *
	 * @param gridCoordinate the gridCoordinate of the FreeCell to check
	 * @return true if and only if the FreeCell at gridCoordinate contains an
	 * Alive Wall
	 */
	public boolean isOnAliveWall(Vector2D gridCoordinate)
	{
		return gameGrid.get(gridCoordinate)
			.getEntitiesOn()
			.stream()
			.anyMatch(entity -> entity instanceof Wall && !((Wall) entity).isDisappearing());
	}

	/**
	 * isOnPowerUp() check whether the gridCoordinate is a FreeCell containing a
	 * PowerUp
	 *
	 * @param gridCoordinate the gridCoordinate of the FreeCell to check
	 * @return true if and only if the FreeCell at gridCoordinate contains a PowerUp
	 */
	private boolean isOnPowerUp(Vector2D gridCoordinate)
	{
		return gameGrid.get(gridCoordinate)
			.getEntitiesOn()
			.stream()
			.anyMatch(entity -> entity instanceof Powerup);
	}

	/**
	 * isAtCrossroad() returns true if and only if the topLeft modelCoordinate
	 * is aligned to the grid pattern and a GameCharacter placed at topLeft could move
	 * in 4/3/2 directions
	 *
	 * @param topLeft topLeftCoordinate of a GameCharacter
	 * @return true if the GameCharacter could move relatively to blocks
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
