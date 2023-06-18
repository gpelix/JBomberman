package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.model.game.entity.Wall;

public class WallOrEnemyDeadState<T extends Entity & StateFul> extends DeadState
{
    private final T entity;

    private final ImageManager deathImageManager;

    public WallOrEnemyDeadState(T entity,
                                ImageManager deathImageManager)
    {
        super(entity);
        this.entity = entity;
        this.deathImageManager = deathImageManager;
    }

    @Override
    public boolean notifyExplosion(Explosion explosion)
    {
        return false; // Simply ignore
    }

    @Override
    public Drawing getDrawing()
    {
        AbstractImage image = deathImageManager.getNextImage();
        if (deathImageManager.hasChanged())
            entity.hasChanged();
        return entity.getDrawing(image);
    }

    @Override
    public void updateInModel()
    {
        Entity entity = getEntity();

        if (getDeathImageManager().hasDoneOneCycle()) {
            GameGrid gameGrid = entity.getGameGrid();

            if (entity instanceof Wall wall)
                gameGrid.get(
                    gameGrid.getCenterInGridCoord(entity)
                ).removeWall(wall);
            else if (entity instanceof Enemy enemy)
                gameGrid.get(
                    gameGrid.getCenterInGridCoord(enemy)
                ).removeCharacter(enemy);
        }
    }

    protected Entity getEntity()
    {
        return entity;
    }

    protected ImageManager getDeathImageManager()
    {
        return deathImageManager;
    }
}
