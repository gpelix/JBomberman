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

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.entity.Block;
import app.jbomberman.model.game.entity.Bomb;
import app.jbomberman.model.game.entity.Door;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.EntityFactory;
import app.jbomberman.model.game.entity.Fire;
import app.jbomberman.model.game.entity.FreeCell;
import app.jbomberman.model.game.entity.GameCharacter;
import app.jbomberman.model.game.entity.Powerup;
import app.jbomberman.model.game.entity.Wall;
import app.jbomberman.util.CallbackOnce;
import app.jbomberman.util.Grid;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.RandomNumberGenerator;
import app.jbomberman.util.Vector2D;
import org.apache.logging.log4j.LogManager;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

/**
 * GameGrid represents the grid of the game. It contains FreeCells, that must
 * contain the other Entities
 *
 * @author gp
 */
public class GameGrid extends Grid<FreeCell>
{
	/**
	 * numberOfEnemies is a variable that keeps the number of enemies in the grid
	 * When the count reaches zero the ALL_ENEMIES_ARE_DEAD_SOUND sound must be
	 * played
	 */
	private int numberOfEnemies = 0;

	/**
	 * MoveManager is the moveManager responsible for the movement of
	 * GameCharacters on the grid
	 */
	private final MoveManager moveManager;

	/**
	 * ALL_ENEMIES_ARE_DEAD_SOUND is the sound to be played when the
	 * numberOfEnemies counter reaches zero
	 */
	private static final String ALL_ENEMIES_ARE_DEAD_SOUND =
		Properties.getInstance().getSoundPath("Bomberman SFX enemies are dead.wav");

	/**
	 * coordinate of the top left corner of the grid
	 */
	private final Vector2D topLeftCorner;

	/**
	 * GameGridBuilder is a reference to the builder. The builder builds the GameGrid,
	 * but it also must be used if new enemies must be spawned (like when a
	 * powerup gets destroyed by a bomb)
	 */
	private final GameGridBuilder gameGridBuilder;

	/**
	 * public constructor of the GameGrid from a vector of dimensions, a vector
	 * specifying the position of the top-left corner of the grid relative to the
	 * Model
	 *
	 * @param dimensions      the dimensions of the grid to be used to initialize
	 *                        the superclass Grid
	 * @param topLeftCorner   the topLeftCorner in model coordinates
	 * @param gameGridBuilder the gameGridBuilder
	 */
	public GameGrid(Vector2D dimensions, Vector2D topLeftCorner, GameGridBuilder gameGridBuilder)
	{
		super(dimensions);
		moveManager = new MoveManager(this);
		this.topLeftCorner = topLeftCorner;
		this.gameGridBuilder = gameGridBuilder;
	}

	/**
	 * deadEnemiesSoundPlayer is a CallbackOnce that must be called when the
	 * numberOfEnemies variable gets to zero
	 */
	private final CallbackOnce deadEnemiesSoundPlayer = new CallbackOnce()
	{
		@Override
		public void run()
		{
			AudioPlayer.getInstance().playSound(ALL_ENEMIES_ARE_DEAD_SOUND);
		}
	};

	/**
	 * update() must be called to update the GameGrid. It recursively calls
	 * update() on every freeCell in the GameGrid
	 */
	public void update()
	{
		if (numberOfEnemies == 0)
			deadEnemiesSoundPlayer.runOnce();

		this.forEach(Entity::updateInModel);
	}

	/**
	 * enemyIsDead() must be called by the enemies whenever an enemy dies
	 */
	public void enemyIsDead()
	{
		numberOfEnemies--;
	}

	/**
	 * getDrawing() gets the drawing of the GameGrid
	 *
	 * @return the drawing of the GameGrid
	 */
	public Drawing getDrawing()
	{
		Spliterator<FreeCell> spliterator =
			Spliterators.spliteratorUnknownSize(iterator(), 0);

		return new Drawing(
			StreamSupport.stream(spliterator, false)
				.map(FreeCell::getDrawing)
				.toArray(Drawing[]::new));
	}

	/**
	 * addGameCharacter() is a function that adds a character to a certain
	 * Freecell
	 *
	 * @param character  the GameCharacter to add to the GameGrid
	 * @param coordinate the coordinate of the FreeCell to which it must be added
	 */
	public void addGameCharacter(GameCharacter character,
															 Vector2D coordinate)
	{
		if (character instanceof Enemy) {
			numberOfEnemies++;
			deadEnemiesSoundPlayer.reset();
		}
		addGameCharacterInFreeCell(character, coordinate);
	}

	/**
	 * addGameCharacterInFreeCell adds a generic GameCharacter to a FreeCell
	 *
	 * @param gameCharacter the GameCharacter to add
	 * @param coordinate    the grid-coordinate to which it must be added
	 */
	public void addGameCharacterInFreeCell(GameCharacter gameCharacter, Vector2D coordinate)
	{
		get(coordinate).addGameCharacter(gameCharacter);
	}

	/**
	 * addWall() adds a Wall to the FreeCell specified by the coordinate
	 *
	 * @param wall       the Wall to add
	 * @param coordinate the coordinate of the FreeCell to which it must be added
	 */
	public void addWall(Wall wall, Vector2D coordinate)
	{
		get(coordinate).addWall(wall);
	}

	/**
	 * addPowerup() adds a Powerup to the FreeCell specified by the coordinate
	 *
	 * @param powerup    the Powerup to add
	 * @param coordinate the coordinate of the FreeCell to which it must be added
	 */
	public void addPowerup(Powerup powerup, Vector2D coordinate)
	{
		get(coordinate).addPowerUp(powerup);
	}

	/**
	 * addDoor() adds a Door to the FreeCell specified by the coordinate
	 *
	 * @param door       the Door to add
	 * @param coordinate the coordinate of the FreeCell to which it must be added
	 */
	public void addDoor(Door door, Vector2D coordinate)
	{
		get(coordinate).addDoor(door);
	}

	/**
	 * addBlock() adds a Block to the FreeCell specified by the coordinate
	 *
	 * @param block      the Block to add
	 * @param coordinate the coordinate of the FreeCell to which it must be added
	 */
	public void addBlock(Block block, Vector2D coordinate)
	{
		get(coordinate).addBlock(block);
	}

	/**
	 * addFire() adds a Fire to the FreeCell specified by the coordinate
	 *
	 * @param fire       the Fire to add
	 * @param coordinate the coordinate of the FreeCell to which it must be added
	 */
	public void addFire(Fire fire, Vector2D coordinate)
	{
		get(coordinate).addFire(fire);
	}

	/**
	 * add#() adds a Bomb to the FreeCell specified by the coordinate
	 *
	 * @param bomb       the Bomb to add
	 * @param coordinate the coordinate of the FreeCell to which it must be added
	 */
	public void addBomb(Bomb bomb, Vector2D coordinate)
	{
		get(coordinate).addBomb(bomb);
	}

	/**
	 * getMoveManager() gets the move manager
	 *
	 * @return the move manager
	 */
	public MoveManager getMoveManager()
	{
		return moveManager;
	}


	/**
	 * moveCharacter() must be called toTopLeft place a GameCharacter toTopLeft a new place in the
	 * grid specifying where it was and where it will be. It essentially updates the sets
	 * that contain entities in the grid
	 *
	 * @param gameCharacter gameCharacter that must be moved. This parameter is mandatory in the case
	 *                      there are more entities on the fromTopLeft coordinate
	 * @param fromTopLeft   the starting topleft model coordinate
	 * @param toTopLeft     the final topLeftModelCoordinate
	 */
	public void moveCharacter(GameCharacter gameCharacter, Vector2D fromTopLeft, Vector2D toTopLeft)
	{

		Vector2D fromGridCoordinate = this.modelCoord2GridCoord(fromTopLeft.sum(Vector2D.of(Entity.SIZE_IN_MODEL / 2, Entity.SIZE_IN_MODEL / 2)));
		Vector2D toGridCoordinate = this.modelCoord2GridCoord(toTopLeft.sum(Vector2D.of(Entity.SIZE_IN_MODEL / 2, Entity.SIZE_IN_MODEL / 2)));
		if (fromGridCoordinate.equals(toGridCoordinate))
			return;

		if (!get(fromGridCoordinate).contains(gameCharacter))
			throw new GamePlayException("Character not found in fromTopLeft position");

		get(fromGridCoordinate).removeCharacter(gameCharacter);
		addGameCharacterInFreeCell(gameCharacter, toGridCoordinate);
	}

	/**
	 * getTopLeftCornerModelCoordinate() returns the topLeftModelCoordinate of the
	 * GameGrid
	 *
	 * @return the topLeftModelCoordinate of the GameGrid
	 */
	public Vector2D getTopLeftCornerModelCoordinate()
	{
		return topLeftCorner;
	}

	/**
	 * return the coordinate
	 *
	 * @param coordinate the model-coordinate of the top left corner
	 * @return the gridCoordinate in which the top left corner is
	 */
	public Vector2D modelCoord2GridCoord(Vector2D coordinate)
	{
		return coordinate.sub(topLeftCorner).div(Entity.SIZE_IN_MODEL);
	}

	/**
	 * Return the top left corner of the bucket
	 *
	 * @param coordinate the grid-coordinate
	 * @return the model-coordinate of the top left corner
	 */
	public Vector2D gridCoord2modelCoor(Vector2D coordinate)
	{
		return coordinate.multiply(Entity.SIZE_IN_MODEL).sum(topLeftCorner);
	}

	/**
	 * it simply sums half the SIZE_IN_MODEL
	 *
	 * @param modelCoordinate the model-coordinate of topLeft
	 * @return the center of the block in model-coordinates
	 */
	public Vector2D getCenter(Vector2D modelCoordinate)
	{
		return modelCoordinate.sum(Vector2D.of(Entity.SIZE_IN_MODEL / 2,
			Entity.SIZE_IN_MODEL / 2));
	}

	/**
	 * get the grid coordinate of CURRENT OCCUPYING location of entity from top
	 * left corner
	 *
	 * @param topLeftCorner the top left corner
	 * @return the center of the lump in grid coordinate
	 */
	public Vector2D getCenterInGridCoord(Vector2D topLeftCorner)
	{
		return this.modelCoord2GridCoord(getCenter(topLeftCorner));
	}

	/**
	 * getNumberOfEnemiesStillAlive() must be called by the Door to check if it
	 * must enable itself or not
	 *
	 * @return the numberOfEnemies currently on the grid
	 */
	public int getNumberOfEnemiesStillAlive()
	{
		return numberOfEnemies;
	}

	/**
	 * doWhenTimeHasEnded() spawns ten new Enemies that will probably cause a lot
	 * of pain to bomberman
	 */
	public void doWhenTimeHasEnded()
	{
		// Spawn 10 random enemies
		for (int i = 0; i < 10; i++) {
			int x, y;
			do {
				x = RandomNumberGenerator.getInstance().nextInt(1, getWidth() - 1);
				y = RandomNumberGenerator.getInstance().nextInt(1, getHeight() - 1);
			} while (!moveManager.canAccessForCommonSizeEntity(
				EntityFactory.getInstance().getEnemyCapabilities(8),
				gridCoord2modelCoor(Vector2D.of(x, y)),
				Vector2D.of(x, y),
				Maybe.nothing()
			));
			gameGridBuilder.addEnemy(Vector2D.of(x, y), 8);
		}
	}

	/**
	 * This function might be removed.
	 *
	 * <p>However, as long as java during development, it's good to see if
	 * there are dangling references that prevent deallocation</p>
	 */
	@Override
	public void finalize()
	{
		LogManager.getLogger(getClass()).info(
			"BomberMan has been GC'ed. Good to know that there are no observer/observable" +
				"problems that prevent deallocation!"
		);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/