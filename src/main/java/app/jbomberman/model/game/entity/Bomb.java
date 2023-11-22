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
 
package app.jbomberman.model.game.entity;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

/**
 * Bomb is an abstract class that represents a Bomb
 *
 * @author gp
 */
public abstract class Bomb extends Entity
{
	/**
	 * BombConstructor is a SAM that has a unique method whose arguments are the
	 * same the only constructor of Bomb.
	 * In this way, we can pass around constructors of Bombs without knowing the
	 * specific type of bomb that will be created
	 *
	 * @see EntityFactory
	 */
	@FunctionalInterface
	public interface BombConstructor
	{
		/**
		 * constructor is the only abstract method
		 *
		 * @param coordinate   the model-coordinate of the Entity
		 * @param gameGrid     the GameGrid to which the Entity is attached
		 * @param bombRange    the range of the bomb
		 * @param bomberMan    the bomberman that spawned the bomb
		 * @param imageManager the ImageManager responsible for displaying the images
		 * @return the constructed Bomb
		 */
		Bomb construct(Vector2D coordinate, int bombRange, BomberMan bomberMan,
									 ImageManager imageManager, GameGrid gameGrid);
	}

	/**
	 * BOMB_DETONATION_SOUND is the String containing the path of the sound to be
	 * played when there is an explosion
	 */
	private static final String BOMB_DETONATION_SOUND =
		Properties.getInstance().getSoundPath("explosion.wav");

	/**
	 * bombRange is the range of the Bomb
	 */
	private final int bombRange;

	/**
	 * ImageManager is the imageManager that will handle the display of the Images
	 */
	private final ImageManager imageManager;

	/**
	 * bomberman is the BomberMan that has spawned the bomb.
	 * When the bomb explodes, the bomberman must be notified
	 */
	private final BomberMan bomberman;

	/**
	 * protected constructor that must be called by subclasses' constructors
	 *
	 * @param coordinate   the model-coordinate of the Entity
	 * @param gameGrid     the GameGrid to which the Entity is attached
	 * @param bombRange    the range of the bomb
	 * @param bomberMan    the bomberman that spawned the bomb
	 * @param imageManager the ImageManager responsible for displaying the images
	 */
	protected Bomb(Vector2D coordinate, int bombRange,
								 BomberMan bomberMan, ImageManager imageManager, GameGrid gameGrid)
	{
		super(coordinate, gameGrid);

		this.bombRange = bombRange;
		this.bomberman = bomberMan;

		this.imageManager = imageManager;
	}

	/**
	 * getDrawing() gets the Drawing of the Bomb,
	 * a new one if the imageManager has changed image,
	 * or simply get the old one
	 *
	 * @return the Drawing of the Bomb
	 */
	@Override
	public Drawing getDrawing()
	{
		if (imageManager.hasChanged())
			hasChanged();
		return getDrawing(imageManager.getImage());
	}

	/**
	 * updateWithExplosion() is the function in charge of making the bomb explode
	 */
	protected void updateWithExplosion()
	{
		FreeCell freeCell = getUnderlyingFreeCell();

		AudioPlayer.getInstance().playSound(BOMB_DETONATION_SOUND);
		bomberman.bombHasExploded(this);
		freeCell.removeBomb(this);
		freeCell.notifyExplosion(new Explosion(bombRange, getCenterInGridCoord()));
	}

	/**
	 * notifyExplosion() simply returns true as unexploded Bombs always block
	 * explosions
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return true as bombs always block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return true;
	}

	/**
	 * toString() is only used for debugging purpose
	 *
	 * @return the String representation of the Entity
	 */
	@Override
	public String toString()
	{
		return "Bomb(" + super.toString() + ")";
	}

	/**
	 * allowsBombs() returns false as no other bombs must be placed on top of
	 * other bombs
	 *
	 * @return false
	 */
	@Override
	public boolean allowsBombs()
	{
		return false;
	}

	/**
	 * makeExplode() is an abstract method that must be called when the player
	 * presses the key to cause an explosion.
	 *
	 * @see AutoBomb
	 * @see ControlledBomb
	 */
	abstract public void makeExplode();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
