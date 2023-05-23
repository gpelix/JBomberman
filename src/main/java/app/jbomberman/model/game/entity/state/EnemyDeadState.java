
package app.jbomberman.model.game.entity.state;

import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.gamecharacter.Enemy;

public class EnemyDeadState extends WallDeadState<Enemy>
{
    public EnemyDeadState(Enemy enemy, ImageManager deathImageManager)
    {
        super(enemy, deathImageManager);
    }

    @Override
    public void updateInModel()
    {
        super.updateInModel();

        if (getDeathImageManager().hasDoneOneCycle()) {
            GameGrid gameGrid = getEntity().getGameGrid();

            gameGrid.enemyIsDead();
        }
    }
}
