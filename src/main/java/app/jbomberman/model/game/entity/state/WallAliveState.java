package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.model.game.entity.Wall;

public class WallAliveState extends AliveState
{
    private final GameCharacterState nextState;

    private final Wall wall;

    private final ImageManager imageManager;

    public WallAliveState(Wall wall, GameCharacterState nextState,
                          ImageManager imageManager)
    {
        super(wall, nextState);
        this.wall = wall;
        this.nextState = nextState;
        this.imageManager = imageManager;
    }

    @Override
    public boolean notifyExplosion(Explosion explosion)
    {
        die();
        return true;
    }

    @Override
    public void updateInModel()
    {
        // Do nothing
    }

    @Override
    public Drawing getDrawing()
    {
        return wall.getDrawing(imageManager.getNextImage());
    }
}
