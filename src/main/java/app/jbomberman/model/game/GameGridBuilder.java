package app.jbomberman.model.game;

import app.jbomberman.model.game.entity.Block;
import app.jbomberman.model.game.entity.BomberMan;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.model.game.entity.EntityFactory;
import app.jbomberman.model.game.entity.FreeCell;
import app.jbomberman.model.game.entity.Powerup;
import app.jbomberman.model.game.entity.Wall;
import app.jbomberman.model.scenes.GameScene;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

/**
 * GameGridBuilder is responsible for building the grid. It must
 * cooperate with GameGridBuilderDirector.
 *
 * @author gp
 */
public class GameGridBuilder
{
    /**
     * grid that is going to be built that is also the final product for
     * GameGridBuilder
     */
    private Maybe<GameGrid> grid;

    /**
     * top left coordinate of the built grid
     */
    private final Vector2D topLeft;

    private final GameScene gameScene;


    public GameGridBuilder(Vector2D topLeft, GameScene gameScene)
    {
        grid = Maybe.nothing();
        this.topLeft = topLeft;
        this.gameScene = gameScene;
    }

    public void setup(Vector2D dimensions)
    {
        grid = Maybe.just(new GameGrid(dimensions, topLeft, this));
    }

    // PLEASE USE ONLY FROM THE DIRECTOR
    public GameGrid getGameGrid()
    {
        return getGrid();
    }

    public GameGrid getResult()
    {
        return getGrid();
    }

    private GameGrid getGrid()
    {
        return grid.unwrapOrThrow(() -> new RuntimeException("Grid not " +
            "build" +
            ". Evidently the builder didn't call setup or did you forget to call " +
            "director.construct()?"));
    }


    public void addEnemy(Vector2D gridCoordinate, int enemyNumber)
    {
        getGrid().addGameCharacter(EntityFactory.getInstance().buildEnemy(
                enemyNumber,
                getGrid().gridCoord2modelCoor(gridCoordinate),
                getGrid()),
            gridCoordinate);
    }

    public void addBlock(Vector2D gridCoordinate)
    {
        getGrid().addBlock(
            new Block(getGrid().gridCoord2modelCoor(gridCoordinate), getGrid()),
            gridCoordinate
        );
    }

    public void addWall(Vector2D gridCoordinate)
    {
        Wall wall = EntityFactory.getInstance().buildWall(
            getGrid().gridCoord2modelCoor(gridCoordinate),
            getGrid()
        );

        getGrid().addWall(
            wall,
            gridCoordinate
        );
    }

    public void addPowerUp(Vector2D gridCoordinate, int powerUpNumber)
    {
        Powerup powerup = EntityFactory.getInstance().buildPowerUp(
            powerUpNumber,
            getGrid().gridCoord2modelCoor(gridCoordinate),
            getGrid()
        );

        getGrid().addPowerup(
            powerup,
            gridCoordinate
        );
    }

    public void addDoor(Vector2D gridCoordinate)
    {
        getGrid().addDoor(
            EntityFactory.getInstance().buildDoor(
                getGameGrid().gridCoord2modelCoor(gridCoordinate),
                getGameGrid()),
            gridCoordinate
        );
    }

    public void addFreeCell(Vector2D gridCoordinate)
    {
        getGrid().set(
            new FreeCell(getGrid().gridCoord2modelCoor(gridCoordinate), getGrid()),
            gridCoordinate
        );
    }

    // Here gameScene acts as a bridge that routes LEFT RIGHT ... to BomberMan
    public void addBomberMan(Vector2D gridCoordinate,
                             BomberManCapabilities capabilities)
    {
        BomberMan bomberMan = EntityFactory.getInstance().buildBomberMan(
            getGrid().gridCoord2modelCoor(gridCoordinate),
            capabilities,
            getGrid());

        // Attach observer bomberman to Scene
        // TODO: This is not what we want as we want filtering by gameScene and
        // TODO: will disable it for now!
        System.out.println("SHHH");
        gameScene.attachObserver(bomberMan);
        // Who will detach it??

        getGrid().addGameCharacter(bomberMan, gridCoordinate);
    }
}
