package app.jbomberman.model.game.entity;

import app.jbomberman.Properties;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

public class AutoBomb extends Bomb
{
    private static final int BOMB_EXPLOSION_INTERVAL = Properties.getInstance().getMaxFps() * 2;

    private int timeToExplosion = BOMB_EXPLOSION_INTERVAL;

    public AutoBomb(Vector2D coordinate, int bombRange,
                    BomberMan bomberMan, ImageManager imageManager,
                    GameGrid gameGrid)
    {
        super(coordinate, bombRange, bomberMan, imageManager, gameGrid);
    }

    @Override
    public void updateInModel()
    {
        if (--timeToExplosion == 0) {
            commonUpdateInModel();
        }
    }

    @Override
    public String toString()
    {
        return "AutoBomb(" + super.toString() + ")";
    }

    @Override
    public boolean allowsBombs()
    {
        return false;
    }
}
