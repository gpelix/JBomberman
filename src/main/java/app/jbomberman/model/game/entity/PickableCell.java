
package app.jbomberman.model.game.entity;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.entity.gamecharacter.BomberMan;
import app.jbomberman.model.game.entity.gamecharacter.Enemy;
import app.jbomberman.util.Vector2D;

import java.util.function.Supplier;

public abstract class PickableCell extends Cell
{
	private final Supplier<Enemy> enemySupplier;

	public PickableCell(Vector2D coordinate, GameGrid gameGrid,
											AbstractImage image, Supplier<Enemy> enemySupplier)
	{
		super(coordinate, image, gameGrid);

		this.enemySupplier = enemySupplier;
	}

	@Override
	public final void updateInModel()
	{
		// Do nothing
	}

	// TODO: this won't work due to priority
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		getGameGrid().addGameCharacter(enemySupplier.get(),
			getGameGrid().getCenterInGridCoord(this));

		return true;
	}

	public abstract void pick(BomberMan bomberMan);
}
