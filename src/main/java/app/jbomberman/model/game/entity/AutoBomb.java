package app.jbomberman.model.game.entity;

import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

/**
 * AutoBomb is a Bomb that detonates automatically
 *
 * @author gp
 */
public class AutoBomb extends Bomb
{
	/**
	 * BOMB_EXPLOSION_INTERVAL is the number of updates needed before the automatic
	 * detonation of the bomb
	 */
	private static final int BOMB_EXPLOSION_INTERVAL = 120;

	/**
	 * timeToExplosion is the number of updates for the AutoBomb that are still
	 * needed before the detonation of the bomb
	 */
	private int timeToExplosion = BOMB_EXPLOSION_INTERVAL;

	/**
	 * public constructor
	 *
	 * @param coordinate   the model-coordinate of the Entity
	 * @param gameGrid     the GameGrid to which the Entity is attached
	 * @param bombRange    the range of the bomb
	 * @param bomberMan    the bomberman that spawned the bomb
	 * @param imageManager the ImageManager responsible for displaying the images
	 */
	public AutoBomb(Vector2D coordinate, int bombRange,
									BomberMan bomberMan, ImageManager imageManager,
									GameGrid gameGrid)
	{
		super(coordinate, bombRange, bomberMan, imageManager, gameGrid);
	}

	/**
	 * updateInModel() does nothing until timeToExplosion reaches zero
	 */
	@Override
	public void updateInModel()
	{
		if (--timeToExplosion == 0)
			updateWithExplosion();
	}

	/**
	 * toString() is only used for debugging purpose
	 *
	 * @return the String representation of the Entity
	 */
	@Override
	public String toString()
	{
		return "AutoBomb(" + super.toString() + ")";
	}

	/**
	 * makeExplode() does nothing, that is it simply ignores the requests to
	 * explode
	 */
	@Override
	public void makeExplode()
	{
		// Do nothing
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
