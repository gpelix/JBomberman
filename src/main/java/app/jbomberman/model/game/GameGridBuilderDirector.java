package app.jbomberman.model.game;

import app.jbomberman.JBomberManException;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.model.game.entity.EntityFactory;
import app.jbomberman.model.game.entity.GameCharacterCapabilities;
import app.jbomberman.model.scenes.GameScene;
import app.jbomberman.util.CollectionUtils;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.RandomNumberGenerator;
import app.jbomberman.util.Resources;
import app.jbomberman.util.Vector2D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameGridBuilderDirector
{
    private static final String LEVEL_JSON_FILE = "json/levels.json";

    private static final int NUMBER_OF_POWERUPS = 8;

    private final ConcreteGameGridBuilder builder;

    /**
     * Width of the grid that must be transmitted to the builder
     */
    private static final int gridWidth = 31;
    /**
     * Height of the grid that must be passed to the builder
     */
    private static final int gridHeight = 15;

    private final int levelNumber;

    private final GameScene gameScene;

    private final BomberManCapabilities account;

    private static final Vector2D bombermanEntryPointGridRelative =
        Vector2D.of(1, 1);

    public GameGridBuilderDirector(GameScene gameScene, ConcreteGameGridBuilder builder,
                                   int levelNumber,
                                   BomberManCapabilities capabilities)
    {
        this.gameScene = gameScene;
        this.builder = builder;
        this.levelNumber = levelNumber;
        this.account = capabilities;
    }

    /**
     * levelAgnosticConstruction is the first fase of the construction of the
     * gameGrid which does not depend on the specific level we are building
     */
    public void levelAgnosticConstruction()
    {
        builder.setup(Vector2D.of(gridWidth, gridHeight));

        for (int i = 0; i < gridWidth; i++)
            for (int j = 0; j < gridHeight; j++)
                builder.addFreeCell(Vector2D.of(i, j));

        // Filling the borders with blocks
        for (int i = 0; i < gridWidth; i++) {
            builder.addBlock(Vector2D.of(i, 0));
            builder.addBlock(Vector2D.of(i, gridHeight - 1));
        }

        for (int i = 1; i < gridHeight - 1; i++) {
            builder.addBlock(Vector2D.of(0, i));
            builder.addBlock(Vector2D.of(gridWidth - 1, i));
        }

        for (int i = 1; i < gridWidth - 1; i++)
            for (int j = 1; j < gridHeight - 1; j++)
                if (i % 2 == 0 && j % 2 == 0)
                    builder.addBlock(Vector2D.of(i, j));
    }

    public void construct()
    {
        levelAgnosticConstruction();

        String readJson = Resources.readResource(LEVEL_JSON_FILE);

        try {
            JSONArray levelSpecs = new JSONArray(readJson);

            if (levelNumber > levelSpecs.length())
                throw new JBomberManException("Unable to find level number `" + levelNumber +
                    "' in file `" + LEVEL_JSON_FILE + "'");

            // levelNumber-1 as levels start at 1 in our game but arrays are
            // indexed starting at zero
            JSONObject thisLevelSpec = levelSpecs.getJSONObject(levelNumber - 1);

            int numberOfWalls = thisLevelSpec.getInt("walls");

            ArrayList<Vector2D> availableCoordinatesForWalls =
                allCoordinatesButBlocks()
                    .collect(Collectors.toCollection(ArrayList::new));

            // Needed afterwards to place powerups and the door
            ArrayList<Vector2D> effectivelyPickedWalls = new ArrayList<>();

            if (numberOfWalls <= 2)
                throw new JBomberManException("Ther number of walls must be " +
                    "greater or equal than 2 but it is " + numberOfWalls);

            while (numberOfWalls-- != 0) {
                if (availableCoordinatesForWalls.isEmpty())
                    throw new JBomberManException(
                        "You specified too many wall for level number " + levelNumber);

                Vector2D coordinate =
                    CollectionUtils.randomPickAndRemove(availableCoordinatesForWalls);

                builder.addWall(coordinate);
                effectivelyPickedWalls.add(coordinate);
            }

            Vector2D placeForDoor =
                CollectionUtils.randomPickAndRemove(effectivelyPickedWalls);

            Vector2D placeForPowerUp =
                CollectionUtils.randomPickAndRemove(effectivelyPickedWalls);

            int powerUpNumber = thisLevelSpec.has("powerUpNumber") ?
                thisLevelSpec.getInt("powerUpNumber") :
                Math.abs(RandomNumberGenerator.getInstance().nextInt()) % NUMBER_OF_POWERUPS + 1;

            builder.addDoor(placeForDoor, gameScene);
            builder.addPowerUp(placeForPowerUp, powerUpNumber);


            JSONArray enemiesArrayJSON = thisLevelSpec.getJSONArray("enemies");

            // We need this as we can determine whether it's ok to place an
            // enemy in a certain place
            GameGrid gameGrid = builder.getGameGrid();
            MoveManager moveManager = gameGrid.getMoveManager();


            //enemies start at 1
            for (int typeOfEnemy = 1; typeOfEnemy <= enemiesArrayJSON.length(); typeOfEnemy++)
            {
                int numberOfEnemies = enemiesArrayJSON.getInt(typeOfEnemy - 1);

                GameCharacterCapabilities capabilities = EntityFactory
                    .getInstance().getCharacterCapabilities(typeOfEnemy);

                // HERE WE NEED TO GET THE MOVE MANAGER AND ASK HIM WHETHER
                // THE POSITION IS OK. WE CANNOT RECYCLE THE PREVIOUS ONES
                // MADE FOR WALLS BECAUSE THERE ARE ENEMIES THAT CAN BE
                // SPAWNED ON WALLS
                ArrayList<Vector2D> availableCoordinates =
                    allCoordinatesButBlocks()
                        .filter(coordinate ->
                            moveManager.canAccessForCommonSizeEntity(
                                capabilities,
                                gameGrid.gridCoord2modelCoor(coordinate),
                                gameGrid.getCenterInGridCoord(coordinate),
                                Maybe.nothing()))
                        .collect(Collectors.toCollection(ArrayList::new));

                for (int k = 0; k < numberOfEnemies; k++) {
                    Vector2D coordinate = CollectionUtils.randomPick(availableCoordinates);
                    builder.addEnemy(coordinate, typeOfEnemy);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JBomberManException("Unable to read and decode correctly " +
                "`" + LEVEL_JSON_FILE + "'. " + e.getMessage());
        }

        builder.addBomberMan(gameScene, bombermanEntryPointGridRelative, account); // These may be
        // changed at will
    }


    public Stream<Vector2D> allGridCoordinates()
    {
        return IntStream.range(0, gridHeight * gridWidth - 1)
            .boxed()
            .map(place -> Vector2D.of(
                place % gridWidth,
                place / gridWidth)
            );
    }

    public Stream<Vector2D> allCoordinatesButBlocks()
    {
        Set<Vector2D> coordinateForBombermanFreedom = new HashSet<>()
        {
            {
                add(bombermanEntryPointGridRelative);
                add(bombermanEntryPointGridRelative.sum(Vector2D.of(0, 1)));
                add(bombermanEntryPointGridRelative.sum(Vector2D.of(1, 0)));
                add(bombermanEntryPointGridRelative.sum(Vector2D.of(0, -1)));
                add(bombermanEntryPointGridRelative.sum(Vector2D.of(-1, 0)));
            }
        };
        return allGridCoordinates().filter(
            coordinate -> {
                int x = coordinate.getX();
                int y = coordinate.getY();

                return 0 < x && x < gridWidth - 1 &&
                    0 < y && y < gridHeight - 1 &&
                    !(x % 2 == 0 && y % 2 == 0) &&
                    coordinateForBombermanFreedom.stream().noneMatch(
                        coord -> coord.equals(coordinate)
                    );
            });
    }
}
