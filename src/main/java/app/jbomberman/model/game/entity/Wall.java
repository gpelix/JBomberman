package app.jbomberman.model.game.entity;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.state.GameCharacterState;
import app.jbomberman.model.game.entity.state.WallAliveState;
import app.jbomberman.model.game.entity.state.WallDeadState;
import app.jbomberman.util.Vector2D;

public class Wall extends Entity
{
    private GameCharacterState<Wall> state;

    public Wall(Vector2D coordinate, GameGrid gameGrid,
                ImageManager aliveImages, ImageManager deathImages)
    {
        super(coordinate, gameGrid);

        state = new WallAliveState(this, new WallDeadState<>(this,
            deathImages), aliveImages);
    }

    @Override
    public void updateInModel()
    {
        state.updateInModel();
    }

    @Override
    public boolean notifyExplosion(Explosion explosion)
    {
        return state.notifyExplosion(explosion);
    }

    @Override
    public Drawing getDrawing()
    {
        return state.getDrawing();
    }

    public void setState(GameCharacterState<Wall> state)
    {
        this.state = state;
    }

    public boolean isDisappearing()
    {
        return state.isDisappearing();
    }
}
