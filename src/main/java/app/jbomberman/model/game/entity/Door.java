package app.jbomberman.model.game.entity;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

import java.util.function.Supplier;

public class Door extends PickableCell
{
	public Door(Vector2D coordinate, AbstractImage image, GameGrid gameGrid,
							Supplier<Enemy> enemySupplier)
	{
		super(coordinate, gameGrid,
			image,
			enemySupplier);
	}

	@Override
	public void pick(BomberMan bomberMan)
	{
		if (getGameGrid().getNumberOfEnemiesStillAlive() == 0) {
			bomberMan.steppedOnDoor(this);
			Model.getInstance().getGame().endGame(true);
		}
	}

	@Override
	public boolean allowsBombs()
	{
		return false;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
