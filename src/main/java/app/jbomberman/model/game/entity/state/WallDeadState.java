package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.Explosion;

public class WallDeadState<T extends Entity> extends DeadState<T>
{
    private final T entity;

    private final ImageManager deathImageManager;

    public WallDeadState(T entity,
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
        return entity.getDrawing(deathImageManager.getNextImage());
    }

    @Override
    public void updateInModel()
    {
        Entity entity = getEntity();

        if (getDeathImageManager().hasDoneOneCycle()) {
            GameGrid gameGrid = entity.getGameGrid();

            gameGrid.get(
                gameGrid.getCenterInGridCoord(entity)
            ).remove(entity);
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
