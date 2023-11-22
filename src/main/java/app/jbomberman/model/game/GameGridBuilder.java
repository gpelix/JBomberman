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

import app.jbomberman.model.Model;
import app.jbomberman.model.game.entity.Block;
import app.jbomberman.model.game.entity.BomberMan;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.model.game.entity.EntityFactory;
import app.jbomberman.model.game.entity.FreeCell;
import app.jbomberman.model.game.entity.Powerup;
import app.jbomberman.model.game.entity.Wall;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

/**
 * GameGridBuilder is responsible for building the grid. It must
 * cooperate with GameGridBuilderDirector.
 *
 * @author gp
 */
public class GameGridBuilder
{
	/**
	 * grid that is going to be built that is also the final product for
	 * GameGridBuilder
	 */
	private Maybe<GameGrid> grid;

	/**
	 * top left coordinate of the built grid
	 */
	private final Vector2D topLeft;

	/**
	 * public constructor from a Vector2D specifying the position of the top-left
	 * corner of the grid in Model coordinates
	 *
	 * @param topLeft the top-left corner of the GameGrid
	 */
	public GameGridBuilder(Vector2D topLeft)
	{
		grid = Maybe.nothing();
		this.topLeft = topLeft;
	}

	/**
	 * When the GameGridBuilder gets created, it's still unknown what the dimensions
	 * of the Grid will be. In fact, the GameGridBuilder must be created before the
	 * GameGridDirector gets going
	 *
	 * @param dimensions the dimensions of the grid
	 */
	public void setup(Vector2D dimensions)
	{
		grid = Maybe.just(new GameGrid(dimensions, topLeft, this));
	}

	/**
	 * getGameGrid() gets the GameGrid
	 *
	 * @return the constructed GameGrid
	 */
	public GameGrid getGrid()
	{
		return grid.unwrapOrThrow(() -> new RuntimeException("Grid not " +
			"build" +
			". Evidently the builder didn't call setup or did you forget to call " +
			"director.construct()?"));
	}

	/**
	 * addEnemy() adds an enemy to the grid in the place specified by
	 * gridCoordinate.
	 *
	 * @param gridCoordinate the coordinate in the grid to which the enemy must be
	 *                       added
	 * @param enemyNumber    the number of the enemy specifying its type
	 */
	public void addEnemy(Vector2D gridCoordinate, int enemyNumber)
	{
		getGrid().addGameCharacter(EntityFactory.getInstance().buildEnemy(
				enemyNumber,
				getGrid().gridCoord2modelCoor(gridCoordinate),
				getGrid()),
			gridCoordinate);
	}

	/**
	 * addBlock() creates a black and adds it to the coordinate specified by
	 * gridCoordinate
	 *
	 * @param gridCoordinate the coordinate of the cell on which the block will
	 *                       have to be placed
	 */
	public void addBlock(Vector2D gridCoordinate)
	{
		getGrid().addBlock(
			new Block(getGrid().gridCoord2modelCoor(gridCoordinate), getGrid()),
			gridCoordinate
		);
	}

	/**
	 * addWall() creates a wall and adds it to the coordinate specified by
	 * gridCoordinate
	 *
	 * @param gridCoordinate the coordinate of the cell on which the wall will
	 *                       have to be placed
	 */
	public void addWall(Vector2D gridCoordinate)
	{
		Wall wall = EntityFactory.getInstance().buildWall(
			getGrid().gridCoord2modelCoor(gridCoordinate),
			getGrid()
		);

		getGrid().addWall(
			wall,
			gridCoordinate
		);
	}

	/**
	 * addPowerUp() adds a powerup to the grid in the place specified by
	 * gridCoordinate.
	 *
	 * @param gridCoordinate the coordinate in the grid to which the powerup
	 *                       must be added
	 * @param powerUpNumber  the number of the powerup specifying its type
	 */
	public void addPowerUp(Vector2D gridCoordinate, int powerUpNumber)
	{
		Powerup powerup = EntityFactory.getInstance().buildPowerUp(
			powerUpNumber,
			getGrid().gridCoord2modelCoor(gridCoordinate),
			getGrid()
		);

		getGrid().addPowerup(
			powerup,
			gridCoordinate
		);
	}

	/**
	 * addDoor() creates a door and adds it to the coordinate specified by
	 * gridCoordinate
	 *
	 * @param gridCoordinate the coordinate of the cell on which the door will
	 *                       have to be placed
	 */
	public void addDoor(Vector2D gridCoordinate)
	{
		getGrid().addDoor(
			EntityFactory.getInstance().buildDoor(
				getGrid().gridCoord2modelCoor(gridCoordinate),
				getGrid()),
			gridCoordinate
		);
	}

	/**
	 * addFreeCell() creates a freeCell and adds it to the coordinate specified by
	 * gridCoordinate
	 *
	 * @param gridCoordinate the coordinate of the grid to which the FreeCell will
	 *                       be placed
	 */
	public void addFreeCell(Vector2D gridCoordinate)
	{
		getGrid().set(
			new FreeCell(getGrid().gridCoord2modelCoor(gridCoordinate), getGrid()),
			gridCoordinate
		);
	}

	/**
	 * addBomberMan() adds bomberman to the freeCell in gridCoordinate with the
	 * capabilities specified in capabilities
	 *
	 * @param gridCoordinate the coordinate of the freecell to which bomberman
	 *                       will be added
	 * @param capabilities   the capabilities bomberman will have
	 */
	public void addBomberMan(Vector2D gridCoordinate,
													 BomberManCapabilities capabilities)
	{
		BomberMan bomberMan = EntityFactory.getInstance().buildBomberMan(
			getGrid().gridCoord2modelCoor(gridCoordinate),
			capabilities,
			getGrid());

		// bomberman will be detached when a new level is loaded
		Model.getInstance().attachObserver(bomberMan);

		getGrid().addGameCharacter(bomberMan, gridCoordinate);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
