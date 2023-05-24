package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.util.Direction;

public class EnemyAliveState extends AliveState<Enemy>
{
    private final Enemy enemy;

    private final GameCharacterState<Enemy> nextState;

    private final MovingImageManager imageManager;

    public EnemyAliveState(Enemy enemy,
                           MovingImageManager imageManager,
                           GameCharacterState<Enemy> nextState)
    {
        super(enemy, nextState);
        this.enemy = enemy;
        this.nextState = nextState;
        this.imageManager = imageManager;
    }

    @Override
    public boolean notifyExplosion(Explosion explosion)
    {
        enemy.getGameGrid().addScore(enemy.getDeathPoints());
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
        return enemy.getDrawing(imageManager.getNextImage(enemy.getPreviousMoveDirection().orElseWrapped(Direction.NORTH)));
    }

    @Override
    public void die()
    {
        enemy.setState(nextState);
    }
}
