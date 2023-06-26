package app.jbomberman.model.game.entity;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

/**
 * Fire is an entity that represents the result of an explosion
 *
 * @author gp
 */
public class Fire extends Entity
{
	/**
	 * FireType if the direction on which the Fire is propagating
	 *
	 * @author gp
	 */
	public enum FireType
	{
		CENTER, HORIZONTAL, VERTICAL, NORTH, SOUTH, EAST, WEST
	}

	/**
	 * imageManager is the ImageManager for the Fire
	 */
	private final ImageManager imageManager;

	/**
	 * protected constructor meant to be called by subclasses' constructors to
	 * initialize the Fire from its model-coordinate, the GameGrid to which it
	 * is attached and its ImageManager
	 *
	 * @param coordinate   the model-coordinate of the Fire
	 * @param gameGrid     the GameGrid to which the Fire is attached
	 * @param imageManager the ImageManager for the Fire
	 */
	public Fire(Vector2D coordinate, GameGrid gameGrid, ImageManager imageManager)
	{
		super(coordinate, gameGrid);

		this.imageManager = imageManager;
	}

	/**
	 * getDrawing() returns the old Drawing if the image hasn't Changed, otherwise
	 * it returns the new one
	 *
	 * @return the new Drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		if (imageManager.hasChanged())
			hasChanged();
		return getDrawing(imageManager.getImage());
	}

	/**
	 * updateInModel() does nothing until the ImageManager has done one cycle of
	 * images
	 */
	@Override
	public void updateInModel()
	{
		if (imageManager.hasDoneOneCycle()) {
			FreeCell freeCell = getUnderlyingFreeCell();
			freeCell.removeFire(this);
		}
	}

	/**
	 * notifyExplosion() returns false as the Fire doesn't Block explosions
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return false
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return false;
	}

	/**
	 * @// TODO: 26/6/23 remove
	 */
	@Override
	public boolean allowsBombs()
	{
		return true;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
