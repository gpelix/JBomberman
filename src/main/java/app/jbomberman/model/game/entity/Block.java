
package app.jbomberman.model.game.entity;

import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

public class Block extends Cell
{
    private static final String img = "block.png";

    public Block(Vector2D coordinate, GameGrid gameGrid)
    {
        super(coordinate,
            FlightWeightImagesFactory.getInstance().getImage(img),
            gameGrid);
    }

    @Override
    public void updateInModel()
    {
        // DO NOTHING
    }

    @Override
    public boolean notifyExplosion(Explosion explosion)
    {
        return true; // Just handle it
    }
}
