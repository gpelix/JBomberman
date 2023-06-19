package app.jbomberman.model.game;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.entity.Block;
import app.jbomberman.model.game.entity.Bomb;
import app.jbomberman.model.game.entity.Door;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.EntityFactory;
import app.jbomberman.model.game.entity.Fire;
import app.jbomberman.model.game.entity.FreeCell;
import app.jbomberman.model.game.entity.GameCharacter;
import app.jbomberman.model.game.entity.Powerup;
import app.jbomberman.model.game.entity.Wall;
import app.jbomberman.util.CallbackOnce;
import app.jbomberman.util.Grid;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.RandomNumberGenerator;
import app.jbomberman.util.Vector2D;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class GameGrid extends Grid<FreeCell> implements Observable
{
    private int numberOfEnemies = 0;

    private final MoveManager moveManager;

    private static final String ALL_ENEMIES_ARE_DEAD_SOUND =
        Properties.getInstance().getSoundPath("Bomberman SFX enemies are dead.wav");

    /**
     * coordinate of the top left corner of the grid
     */
    private final Vector2D topLeftCorner;

    private final GameGridBuilder gameGridBuilder;

    public GameGrid(Vector2D dimensions, Vector2D topLeftCorner, GameGridBuilder gameGridBuilder)
    {
        super(dimensions);
        moveManager = new MoveManager(this);
        this.topLeftCorner = topLeftCorner;
        this.gameGridBuilder = gameGridBuilder;
    }


    private final CallbackOnce deadEnemiesSoundPlayer = new CallbackOnce()
    {
        @Override
        public void run()
        {
            AudioPlayer.getInstance().playSound(ALL_ENEMIES_ARE_DEAD_SOUND);
        }
    };

    public void update()
    {
        if (numberOfEnemies == 0)
            deadEnemiesSoundPlayer.runOnce();

        this.forEach(Entity::updateInModel);
    }

    public void enemyIsDead()
    {
        numberOfEnemies--;
    }

    public Drawing getDrawing()
    {
        Spliterator<FreeCell> spliterator =
            Spliterators.spliteratorUnknownSize(iterator(), 0);

        return new Drawing(
            StreamSupport.stream(spliterator, false)
                .map(FreeCell::getDrawing)
                .toArray(Drawing[]::new));
    }

    public void addGameCharacter(GameCharacter character,
                                 Vector2D coordinate)
    {
        if (character instanceof Enemy) {
            numberOfEnemies++;
            deadEnemiesSoundPlayer.reset();
        }
        addGameCharacterInFreeCell(character, coordinate);
    }

    public void addGameCharacterInFreeCell(GameCharacter gameCharacter, Vector2D coordinate)
    {
        get(coordinate).addGameCharacter(gameCharacter);
    }

    public void addWall(Wall wall, Vector2D coordinate)
    {
        get(coordinate).addWall(wall);
    }

    public void addPowerup(Powerup powerup, Vector2D coordinate)
    {
        get(coordinate).addPowerUp(powerup);
    }

    public void addDoor(Door door, Vector2D coordinate)
    {
        get(coordinate).addDoor(door);
    }

    public void addBlock(Block block, Vector2D coordinate)
    {
        get(coordinate).addBlock(block);
    }

    public void addFire(Fire fire, Vector2D coordinate)
    {
        get(coordinate).addFire(fire);
    }

    public void addBomb(Bomb bomb, Vector2D coordinate)
    {
        get(coordinate).addBomb(bomb);
    }

    public MoveManager getMoveManager()
    {
        return moveManager;
    }


    /**
     * moveCharacter() must be called toTopLeft place a GameCharacter toTopLeft a new place in the
     * grid specifying where it was and where it will be. It essentially updates the sets
     * that contain entities in the grid
     *
     * @param gameCharacter gameCharacter that must be moved. This parameter is mandatory in the case
     *                      there are more entities on the fromTopLeft coordinate
     * @param fromTopLeft
     * @param toTopLeft
     */
    public void moveCharacter(GameCharacter gameCharacter, Vector2D fromTopLeft, Vector2D toTopLeft)
    {

        Vector2D fromGridCoordinate = this.modelCoord2GridCoord(fromTopLeft.sum(Vector2D.of(Entity.SIZE_IN_MODEL / 2, Entity.SIZE_IN_MODEL / 2)));
        Vector2D toGridCoordinate = this.modelCoord2GridCoord(toTopLeft.sum(Vector2D.of(Entity.SIZE_IN_MODEL / 2, Entity.SIZE_IN_MODEL / 2)));
        if (fromGridCoordinate.equals(toGridCoordinate))
            return;

        if (!get(fromGridCoordinate).contains(gameCharacter))
            throw new GamePlayException("Character not found in fromTopLeft position");

        get(fromGridCoordinate).removeCharacter(gameCharacter);
        addGameCharacterInFreeCell(gameCharacter, toGridCoordinate);
    }

    public Vector2D getTopLeftCornerModelCoordinate()
    {
        return topLeftCorner;
    }

    /**
     * return the coordinate
     */
    public Vector2D modelCoord2GridCoord(Vector2D coordinate)
    {
        return coordinate.sub(topLeftCorner).div(Entity.SIZE_IN_MODEL);
    }

    /**
     * Return the top left corner of bucket
     */
    public Vector2D gridCoord2modelCoor(Vector2D coordinate)
    {
        return coordinate.multiply(Entity.SIZE_IN_MODEL).sum(topLeftCorner);
    }

    /**
     * it simply sums half the SIZE_IN_MODEL
     */
    public Vector2D getCenter(Vector2D modelCoordinate)
    {
        return modelCoordinate.sum(Vector2D.of(Entity.SIZE_IN_MODEL / 2,
            Entity.SIZE_IN_MODEL / 2));
    }

    /**
     * get the grid coordinate of CURRENT OCCUPYING location of entity from top
     * left corner
     */
    public Vector2D getCenterInGridCoord(Vector2D topLeftCorner)
    {
        return this.modelCoord2GridCoord(getCenter(topLeftCorner));
    }

    public void powerUpTaken()
    {
        updateAllObservers(); // Tell the gameScene that a powerup has been picked
    }

    private final Set<Observer> observers = new HashSet<>();

    @Override
    public Collection<Observer> getObservers()
    {
        return observers;
    }

    public int getNumberOfEnemiesStillAlive()
    {
        return numberOfEnemies;
    }

    public void timeHasEnded()
    {
        // Spawn 10 random enemies
        for (int i = 0; i < 10; i++) {
            int x, y;
            do {
                x = RandomNumberGenerator.getInstance().nextInt(1, getWidth() - 1);
                y = RandomNumberGenerator.getInstance().nextInt(1, getHeight() - 1);
            } while (!moveManager.canAccessForCommonSizeEntity(
                EntityFactory.getInstance().getCharacterCapabilities(8),
                gridCoord2modelCoor(Vector2D.of(x, y)),
                Vector2D.of(x, y),
                Maybe.nothing()
            ));
            gameGridBuilder.addEnemy(Vector2D.of(x, y), 8);
        }
    }
}
