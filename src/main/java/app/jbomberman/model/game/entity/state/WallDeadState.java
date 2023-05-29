package app.jbomberman.model.game.entity.state;

import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Wall;

public class WallDeadState extends WallOrEnemyDeadState<Wall>
{
    public WallDeadState(Wall entity,
                         ImageManager deathImageManager)
    {
        super(entity, deathImageManager);
    }
}
