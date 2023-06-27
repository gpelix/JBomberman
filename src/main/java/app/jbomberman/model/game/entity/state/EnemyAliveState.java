package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.util.Direction;

/**
 * EnemyAliveState is the class that represents the state of an Enemy alive
 *
 * @author gp
 */
public class EnemyAliveState extends AliveState
{
	/**
	 * enemy that has this as state
	 */
	private final Enemy enemy;

	/**
	 * imageManager is enemy's ImageManager
	 */
	private final MovingImageManager imageManager;

	/**
	 * public constructor of EnemyAliveState
	 *
	 * @param enemy        the Enemy that has this as state
	 * @param imageManager the ImageManager of the Enemy
	 * @param nextState    the state to set when the Enemy dies
	 */
	public EnemyAliveState(Enemy enemy,
												 MovingImageManager imageManager,
												 AbstractState nextState)
	{
		super(enemy, nextState);
		this.enemy = enemy;
		this.imageManager = imageManager;
	}

	/**
	 * notifyExplosion() must be called to notify the Enemy that there has been an
	 * explosion
	 *
	 * @param explosion the Explosion object
	 * @return false as enemies don't block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		Model.getInstance().getAccount().getCapabilities().addPoints(enemy.getDeathPoints());
		die();
		return false;
	}

	/**
	 * updateInModel() moves the enemy
	 */
	@Override
	public void updateInModel()
	{
		enemy.move();
	}

	/**
	 * updateInModel() gets the Drawing of the Enemy.
	 *
	 * @return the Drawing of the Enemy
	 */
	@Override
	public Drawing getDrawing()
	{
		// enemies always move and always change there image
		enemy.hasChanged();
		return enemy.getDrawing(imageManager.getImage(enemy.getPreviousMoveDirection().orElseWrapped(Direction.NORTH)));
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
