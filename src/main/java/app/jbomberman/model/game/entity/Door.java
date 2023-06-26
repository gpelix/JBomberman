package app.jbomberman.model.game.entity;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

import java.util.function.Supplier;

/**
 * Door is a PickableCell that represents a Door to the next level in the game
 *
 * @author gp
 */
public class Door extends PickableCell
{
	/**
	 * public constructor of Door
	 *
	 * @param coordinate    the model-coordinate of the Door
	 * @param gameGrid      the GameGrid to which the Door is attached
	 * @param image         the image of the Door
	 * @param enemySupplier the supplier that will construct the Enemy when
	 *                      requested
	 */
	public Door(Vector2D coordinate, AbstractImage image, GameGrid gameGrid,
							Supplier<Enemy> enemySupplier)
	{
		super(coordinate, gameGrid, image, enemySupplier);
	}

	/**
	 * pick() stops the current level and tells the Model that the Game has ended
	 * with a victory
	 *
	 * @param bomberMan the instance of Bomberman that picked the PickableCell
	 */
	@Override
	public void pick(BomberMan bomberMan)
	{
		if (getGameGrid().getNumberOfEnemiesStillAlive() == 0) {
			bomberMan.steppedOnDoor(this);
			Model.getInstance().getGame().endGame(true);
		}
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
