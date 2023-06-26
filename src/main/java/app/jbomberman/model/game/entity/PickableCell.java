package app.jbomberman.model.game.entity;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

import java.util.function.Supplier;

/**
 * PickableCell is a class modeling every kind of Cell that can be picked by
 * BomberMan
 *
 * @author gp
 */
public abstract class PickableCell extends Cell
{
	/**
	 * enemySupplier is a Supplier that constructs an Enemy when requested
	 */
	private final Supplier<Enemy> enemySupplier;

	/**
	 * public constructor of PickableCell
	 *
	 * @param coordinate    the model-coordinate of the PiackbleCell
	 * @param gameGrid      the GameGrid to which the PickableCell is attached
	 * @param image         the image of the PickableCell
	 * @param enemySupplier the supplier that will construct the Enemy when
	 *                      requested
	 */
	public PickableCell(Vector2D coordinate, GameGrid gameGrid,
											AbstractImage image, Supplier<Enemy> enemySupplier)
	{
		super(coordinate, image, gameGrid);

		this.enemySupplier = enemySupplier;
	}

	/**
	 * updateInModel() does nothing as the PickableCell never changes state
	 */
	@Override
	public final void updateInModel()
	{
		// Do nothing
	}

	/**
	 * allowsBombs() always returns false as PickableCells don't allow Bomberman
	 * placing bombs on them
	 *
	 * @return false
	 */
	@Override
	public boolean allowsBombs()
	{
		return false;
	}

	/**
	 * notifyExplosion spawns the enemy got from the enemySupplier
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return true as PickableCells always block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		getGameGrid().addGameCharacter(enemySupplier.get(),
			getGameGrid().getCenterInGridCoord(this));

		return true;
	}

	/**
	 * pick() must be called when the PickableCell gets picked by Bomberman
	 *
	 * @param bomberMan the instance of Bomberman that picked the PickableCell
	 */
	public abstract void pick(BomberMan bomberMan);
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
