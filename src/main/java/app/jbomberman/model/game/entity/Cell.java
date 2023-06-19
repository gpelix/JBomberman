package app.jbomberman.model.game.entity;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

public abstract class Cell extends Entity
{
    public Cell(Vector2D coordinate, AbstractImage img, GameGrid gameGrid)
    {
        super(coordinate, gameGrid);
        this.img = img;
    }

    private final AbstractImage img;


    @Override
    public Drawing getDrawing()
    {
        return getDrawing(img);
    }
}
