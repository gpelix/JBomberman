package app.jbomberman.model.game.entity;

import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

/**
 * ControllerBomb is a Bomb that will explode only when the player requests it
 *
 * @author gp
 */
public class ControlledBomb extends Bomb
{
	/**
	 * doDetonation is a flag set to true if and only if at next call to
	 * updateInModel() it must explode
	 */
	private boolean doDetonation;

	/**
	 * public constructor of ControllerBombs
	 *
	 * @param coordinate   the model-coordinate of the Entity
	 * @param gameGrid     the GameGrid to which the Entity is attached
	 * @param bombRange    the range of the bomb
	 * @param bomberMan    the bomberman that spawned the bomb
	 * @param imageManager the ImageManager responsible for displaying the images
	 */
	public ControlledBomb(Vector2D coordinate, int bombRange, BomberMan bomberMan,
												ImageManager imageManager, GameGrid gameGrid)
	{
		super(coordinate, bombRange, bomberMan, imageManager, gameGrid);

		doDetonation = false;
	}

	/**
	 * updateInModel() does nothing until doDetonation is true
	 */
	@Override
	public void updateInModel()
	{
		if (doDetonation)
			super.updateWithExplosion();
	}

	/**
	 * makeExplodes() sets the doDetonation flag, and, next time updateInModel()
	 * gets called, it will make the bomb explode
	 */
	@Override
	public void makeExplode()
	{
		doDetonation = true;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
