package app.jbomberman.model.game.entity.state;

import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Enemy;

/**
 * EnemyDeadState is the state of a dying Enemy
 *
 * @author gp
 */
public class EnemyDeadState extends SimpleDeadState<Enemy>
{
	/**
	 * public constructor of EnemyDeadState
	 *
	 * @param enemy             the Enemy whose state is this
	 * @param deathImageManager the ImageManager that will handle the death
	 *                          sequence
	 */
	public EnemyDeadState(Enemy enemy, ImageManager deathImageManager)
	{
		super(enemy, deathImageManager);
	}

	/**
	 * updateInModel() does nothing until the ImageManager has done one cycle.
	 * In that case, it removes the Enemy and signals the GameGrid that an enemy
	 * is dead
	 */
	@Override
	public void updateInModel()
	{
		super.updateInModel();

		if (getDeathImageManager().hasDoneOneCycle())
			getEntity().getGameGrid().enemyIsDead();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
