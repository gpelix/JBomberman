package app.jbomberman.model.game.entity;

import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

public class ControlledBomb extends Bomb
{
    private boolean doDetonation;

    public ControlledBomb(Vector2D coordinate, int bombRange, BomberMan bomberMan,
                          ImageManager imageManager, GameGrid gameGrid)
    {
        super(coordinate, bombRange, bomberMan, imageManager, gameGrid);

        doDetonation = false;
    }

    @Override
    public void updateInModel()
    {
        if (doDetonation)
            super.commonUpdateInModel();
    }

    @Override
    public void makeExplode()
    {
        doDetonation = true;
    }
}
