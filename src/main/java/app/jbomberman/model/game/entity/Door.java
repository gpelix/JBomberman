package app.jbomberman.model.game.entity;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.entity.gamecharacter.BomberMan;
import app.jbomberman.model.game.entity.gamecharacter.Enemy;
import app.jbomberman.model.scenes.GameScene;
import app.jbomberman.util.Vector2D;

import java.util.function.Supplier;

public class Door extends PickableCell
{
    private final GameScene gameScene;

    public Door(Vector2D coordinate, AbstractImage image, GameGrid gameGrid,
                Supplier<Enemy> enemySupplier, GameScene gameScene)
    {
        super(coordinate, gameGrid,
            image,
            enemySupplier);

        this.gameScene = gameScene;
    }

    @Override
    public void pick(BomberMan bomberMan)
    {
        if (getGameGrid().getNumberOfEnemiesStillAlive() == 0) {
            bomberMan.getCapabilities().addLife();
            bomberMan.stopListening();
            bomberMan.getValuesFrom(this); // Attract bomberman
            gameScene.endGame(true);
        }
    }
}
