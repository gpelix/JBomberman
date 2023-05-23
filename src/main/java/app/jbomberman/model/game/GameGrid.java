package app.jbomberman.model.game;

import app.jbomberman.JBomberManException;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.FreeCell;
import app.jbomberman.model.game.entity.gamecharacter.BomberMan;
import app.jbomberman.model.game.entity.gamecharacter.BomberManCapabilities;
import app.jbomberman.model.game.entity.gamecharacter.Enemy;
import app.jbomberman.model.game.entity.gamecharacter.GameCharacter;
import app.jbomberman.model.scenes.GameScene;
import app.jbomberman.util.Grid;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
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

    boolean hasReachedZero = false;

    // This field must be attached by the constructor
    private BomberMan bomberMan;

    private GameScene scene;

    private final MoveManager moveManager;

    private static final String ALL_ENEMIES_ARE_DEAD_SOUND =
        "sounds/Bomberman SFX enemies are dead.wav";

    /**
     * coordinate of the top left corner of the grid
     */
    private final Vector2D topLeftCorner;

    public GameGrid(Vector2D dimensions, Vector2D topLeftCorner)
    {
        super(dimensions);
        moveManager = new MoveManager(this);
        this.topLeftCorner = topLeftCorner;
    }

    public void update()
    {
        if (numberOfEnemies == 0 && !hasReachedZero) {
            AudioPlayer.getInstance().playSound(ALL_ENEMIES_ARE_DEAD_SOUND);
            hasReachedZero = true;
        }

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
            hasReachedZero = false;
        }
        addEntityInFreeCell(character, coordinate);
    }

    /**
     * @param entity
     * @param coordinate
     */
    public void addEntityInFreeCell(Entity entity, Vector2D coordinate)
    {
        get(coordinate).addEntity(entity);
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

        get(fromGridCoordinate).remove(gameCharacter);
        get(toGridCoordinate).addEntity(gameCharacter);
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
        updateAllObservers(); // Tell the gameGrid that a powerup has been picked
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

    @Deprecated
    public void setScene(GameScene scene)
    {
        this.scene = scene;
    }

    @Deprecated
    public GameScene getScene()
    {
        return scene;
    }

    public void timeHasEnded()
    {
        // TODO: sdfkldsfkljfkljds
        throw new JBomberManException("Time has ended");
    }

    public int getScore()
    {
        return bomberMan.getCapabilities().getPointsEarned();
    }

    public void addScore(int points)
    {
        BomberManCapabilities capabilities = bomberMan.getCapabilities();

        capabilities.addPoints(points);
    }

    public void attachBomberman(BomberMan bomberMan)
    {
        this.bomberMan = bomberMan;
    }

    public BomberMan getBomberMan()
    {
        return bomberMan;
    }
}
