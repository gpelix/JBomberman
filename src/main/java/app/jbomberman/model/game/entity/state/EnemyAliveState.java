package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.util.Direction;

public class EnemyAliveState extends AliveState
{
	private final Enemy enemy;

	private final MovingImageManager imageManager;

	public EnemyAliveState(Enemy enemy,
												 MovingImageManager imageManager,
												 GameCharacterState nextState)
	{
		super(enemy, nextState);
		this.enemy = enemy;
		this.imageManager = imageManager;
	}

	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		Model.getInstance().getAccount().getCapabilities().addPoints(enemy.getDeathPoints());
		die();
		return false;
	}

	@Override
	public void updateInModel()
	{
		enemy.move();
	}

	@Override
	public Drawing getDrawing()
	{
		enemy.hasChanged();
		return enemy.getDrawing(imageManager.getNextImage(enemy.getPreviousMoveDirection().orElseWrapped(Direction.NORTH)));
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
