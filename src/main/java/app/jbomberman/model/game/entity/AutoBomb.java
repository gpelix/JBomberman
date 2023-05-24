package app.jbomberman.model.game.entity;

import app.jbomberman.Settings;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.gamecharacter.BomberMan;
import app.jbomberman.util.Vector2D;

public class AutoBomb extends Bomb
{
    private static final int BOMB_EXPLOSION_INTERVAL = Settings.getInstance().getMaxFps() * 2;

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
            super.commonUpdateInModel();
        }
    }

    @Override
    public String toString()
    {
        return "AutoBomb(" + super.toString() + ")";
    }
}
