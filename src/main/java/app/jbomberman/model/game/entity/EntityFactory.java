package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.game.FullMovementMovingImageManager;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.LeftRightMovingImageManager;
import app.jbomberman.util.Resources;
import app.jbomberman.util.Vector2D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityFactory
{
    private static EntityFactory factory;

    private static final String CHARACTER_SPECIFICATION_FILE =
        "json/entitiesSpecs.json";

    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String UP = "up";
    private static final String DOWN = "down";

    private static final String ALIVE = "alive";

    private static final String DEATH = "death";

    private static final String SPEED = "speed";
    private static final String GOES_THROUGH_WALLS_KEY = "goesThroughWalls";

    public static EntityFactory getInstance()
    {
        if (factory == null)
            factory = new EntityFactory();

        return factory;
    }

    private final JSONObject entitiesSpecification;

    private EntityFactory()
    {
        try {
            entitiesSpecification =
                new JSONObject(Resources.readResource(CHARACTER_SPECIFICATION_FILE));
        } catch (JSONException e) {
            throw new RuntimeException(
                "JSONException was thrown. Format error in file " + CHARACTER_SPECIFICATION_FILE
            );
        }
    }

    private JSONObject getEnemySpecification(int enemyNumber)
    {
        return entitiesSpecification.getJSONObject("Enemy" + enemyNumber);
    }

    public Enemy buildEnemy(int enemyNumber, Vector2D screenCoordinate,
                            GameGrid gameGrid)
    {
        try {
            JSONObject enemySpecificationObject =
                getEnemySpecification(enemyNumber);

            double speed = enemySpecificationObject.getDouble(SPEED);

            boolean goesThroughWalls = enemySpecificationObject.getBoolean(
                GOES_THROUGH_WALLS_KEY);

            List<AbstractImage> deathImages =
                getImagesList(enemySpecificationObject, DEATH);

            int deathAwardingPoints = enemySpecificationObject.getInt("points");

            deathImages.add(FlightWeightImagesFactory.getInstance()
                .getImage(deathAwardingPoints + ".png"));

            return new Enemy(screenCoordinate, speed, goesThroughWalls,
                gameGrid,
                new LeftRightMovingImageManager(
                    getImagesList(enemySpecificationObject, LEFT),
                    getImagesList(enemySpecificationObject, RIGHT)),
                new ImageManager(deathImages, 0),
                deathAwardingPoints);
        } catch (JSONException e) {
            throw new JBomberManException(
                "Json format error in specificationFile " + e.getMessage());
        }
    }

    public Powerup buildPowerUp(int powerupNumber, Vector2D modelCoordinate,
                                GameGrid gameGrid)
    {
        try {
            JSONObject powerupSpecification =
                entitiesSpecification.getJSONObject("powerup" + powerupNumber);

            int enemyNumber = powerupSpecification.getInt(
                "enemyAfterExplosion");

            AbstractImage image = FlightWeightImagesFactory.getInstance().getImage(
                powerupSpecification.getString("img")
            );

            JSONObject capabilitiesJson = powerupSpecification.getJSONObject(
                "addedCapabilities");

            BomberManCapabilities addedCapabilities = new BomberManCapabilities(
                capabilitiesJson.getDouble("speed"),
                capabilitiesJson.getInt("bombRange"),
                capabilitiesJson.getInt("availableBombs"),
                capabilitiesJson.getBoolean("remoteControl"),
                capabilitiesJson.getBoolean("goesThroughWalls"),
                capabilitiesJson.getBoolean("goesThroughBombs"),
                capabilitiesJson.getBoolean("goesThroughFire"),
                capabilitiesJson.getBoolean("isInvincible"),
                0, // For now no powerup causes an increase of score or an addition of lives
                0
            );

            return new Powerup(modelCoordinate, gameGrid,
                image,
                buildEnemy(enemyNumber, modelCoordinate, gameGrid),
                addedCapabilities);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JBomberManException(
                "Json format error in specificationFile " + e.getMessage());
        }
    }

    public Entity buildDoor(Vector2D modelCoordinate, GameGrid gameGrid)
    {
        try {
            JSONObject doorSpec = entitiesSpecification.getJSONObject("door");

            AbstractImage image = FlightWeightImagesFactory.getInstance().getImage(
                doorSpec.getString("img")
            );

            return new Door(modelCoordinate,
                image,
                gameGrid,
                () -> buildEnemy(2, modelCoordinate, gameGrid));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JBomberManException(
                "Json format error in specificationFile " + e.getMessage());
        }
    }

    public Wall buildWall(Vector2D screenCoordinate, GameGrid gameGrid)
    {
        try {
            JSONObject wallSpecificationObject =
                entitiesSpecification.getJSONObject("wall");

            ImageManager deathImages = new ImageManager(getImagesList(wallSpecificationObject,
                DEATH), 0);

            deathImages.setUpdateInterval(5); // This should be fine

            return new Wall(
                screenCoordinate,
                gameGrid,
                new ImageManager(getImagesList(wallSpecificationObject,
                    ALIVE), 0),
                deathImages
            );
        } catch (JSONException e) {
            throw new JBomberManException(
                "Json format error in specificationFile " + e.getMessage());
        }
    }

    public BomberMan buildBomberMan(Vector2D screenCoordinate,
                                    BomberManCapabilities capabilities,
                                    GameGrid gameGrid)
    {
        try {
            JSONObject bomberManSpecification =
                entitiesSpecification.getJSONObject("bomberMan");

            return new BomberMan(screenCoordinate, capabilities,
                gameGrid,
                new FullMovementMovingImageManager(
                    getImagesList(bomberManSpecification, LEFT),
                    getImagesList(bomberManSpecification, RIGHT),
                    getImagesList(bomberManSpecification, UP),
                    getImagesList(bomberManSpecification, DOWN)),
                new ImageManager(getImagesList(bomberManSpecification, DEATH)
                    , 0)
            );
        } catch (JSONException e) {
            throw new JBomberManException(
                "Json format error in specificationFile" + e.getMessage());
        }
    }

    public Bomb buildBomb(Vector2D coordinate,
                          int bombRange,
                          BomberMan bomberMan,
                          GameGrid gameGrid,
                          Bomb.BombConstructor<? extends Bomb> constructor)
    {
        try {
            JSONArray bombSpecification =
                entitiesSpecification.getJSONArray("bomb");

            return constructor.construct(coordinate, bombRange, bomberMan,
                new ImageManager(getImageListFromJsonArray(bombSpecification)
                    , 0), gameGrid);
        } catch (JSONException e) {
            throw new JBomberManException(
                "Json format error in specificationFile" + e.getMessage());
        }
    }


    public Entity buildFire(Fire.FireType fireType, Vector2D coordinate,
                            GameGrid gameGrid)
    {
        try {
            JSONArray fireSpecification =
                entitiesSpecification.getJSONObject("fire").getJSONArray(fireType.toString());

            ImageManager imageManager = new ImageManager(
                getImageListFromJsonArray(fireSpecification), 0);
            imageManager.setUpdateInterval(5); // Half the default time should be ok

            return new Fire(coordinate, gameGrid, imageManager);

        } catch (JSONException e) {
            throw new JBomberManException(
                "Json format error in specificationFile" + e.getMessage());
        }
    }

    public GameCharacterCapabilities getCharacterCapabilities(int enemyNumber)
    {
        JSONObject enemySpecificationObject =
            getEnemySpecification(enemyNumber);

        double speed = enemySpecificationObject.getDouble(SPEED);

        boolean goesThroughWalls = enemySpecificationObject.getBoolean(
            GOES_THROUGH_WALLS_KEY);

        return new GameCharacterCapabilities(goesThroughWalls, speed);
    }

    private List<AbstractImage> getImagesList(JSONObject specification, String key)
    {
        return getImageListFromJsonArray(specification.getJSONArray(key));
    }

    private List<AbstractImage> getImageListFromJsonArray(JSONArray jsonArrayListOfImages)
    {
        List<String> images = new ArrayList<>();

        for (int i = 0; i < jsonArrayListOfImages.length(); i++)
            images.add(jsonArrayListOfImages.getString(i));

        return images.stream().map(FlightWeightImagesFactory.getInstance()::getImage)
            .collect(Collectors.toList());
    }
}
