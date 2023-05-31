package app.jbomberman.model.game.entity;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

public class Fire extends Entity
{
    public enum FireType
    {
        CENTER, HORIZONTAL, VERTICAL, NORTH, SOUTH, EAST, WEST
    }

    private final ImageManager imageManager;

    public Fire(Vector2D coordinate, GameGrid gameGrid, ImageManager imageManager)
    {
        super(coordinate, gameGrid);

        this.imageManager = imageManager;
    }

    @Override
    public Drawing getDrawing()
    {
        return getDrawing(imageManager.getNextImage());
    }

    @Override
    public void updateInModel()
    {
        if (imageManager.hasDoneOneCycle()) {
            FreeCell freeCell = getUnderlyingFreeCell();
            freeCell.remove(this);
        }
    }

    @Override
    public boolean notifyExplosion(Explosion explosion)
    {
        return false;
    }
}
