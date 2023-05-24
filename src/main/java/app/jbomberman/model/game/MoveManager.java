package app.jbomberman.model.game;

import app.jbomberman.model.game.entity.Block;
import app.jbomberman.model.game.entity.Bomb;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.Powerup;
import app.jbomberman.model.game.entity.Wall;
import app.jbomberman.model.game.entity.gamecharacter.BomberManCapabilities;
import app.jbomberman.model.game.entity.gamecharacter.GameCharacterCapabilities;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.stream.Stream;

public class MoveManager
{
    private final GameGrid gameGrid;

    public MoveManager(GameGrid gameGrid)
    {
        this.gameGrid = gameGrid;
    }

    public Vector2D getTopRight(Vector2D coordinate)
    {
        return Vector2D.of(coordinate.getX() + Entity.SIZE_IN_MODEL - 1,
            coordinate.getY());
    }

    public Vector2D getBottomRight(Vector2D coordinate)
    {
        return Vector2D.of(coordinate.getX() + Entity.SIZE_IN_MODEL - 1,
            coordinate.getY() + Entity.SIZE_IN_MODEL - 1);
    }

    public Vector2D getBottomLeft(Vector2D coordinate)
    {
        return Vector2D.of(coordinate.getX(),
            coordinate.getY() + Entity.SIZE_IN_MODEL - 1);
    }

    // Take this coordinate, rescale it to grid size and see if it's in
    public boolean isInsideGrid(Vector2D coordinate)
    {
        return gameGrid.isWithinBoundaries(gameGrid.modelCoord2GridCoord(coordinate));
    }

    /**
     * @param finalPlaceTopLeft gameCharacter's top left coordinate where it
     *                          wants to go
     * @return a boolean that says if the gameCharacter can be moved to (x, y) or not
     */
    public boolean canAccessForCommonSizeEntity(GameCharacterCapabilities capabilities,
                                                Vector2D finalPlaceTopLeft,
                                                Vector2D currentEntityGameGridCoordinate,
                                                Maybe<Direction> moveDirection)
    {
        return generalCanAccess(Stream.of(getBottomRight(finalPlaceTopLeft),
                getTopRight(finalPlaceTopLeft),
                finalPlaceTopLeft, getBottomLeft(finalPlaceTopLeft)),
            capabilities, currentEntityGameGridCoordinate, moveDirection);
    }

    public boolean canAccessForSmallEntity(BomberManCapabilities capabilities,
                                           Vector2D finalPlaceTopLeft,
                                           Vector2D currentEntityGameGridCoordinate,
                                           Direction moveDirection)
    {
        Stream<Vector2D> coordinates = moveDirection.isHorizontal() ?
            Stream.of(
                getBottomLeft(finalPlaceTopLeft).sum(Vector2D.of(0, -PIXEL_OFFSET)),
                getBottomRight(finalPlaceTopLeft).sum(Vector2D.of(0, -PIXEL_OFFSET)),
                finalPlaceTopLeft.sum(Vector2D.of(0, PIXEL_OFFSET)),
                getTopRight(finalPlaceTopLeft).sum(Vector2D.of(0, PIXEL_OFFSET))
            ) : Stream.of(
            getBottomLeft(finalPlaceTopLeft).sum(Vector2D.of(PIXEL_OFFSET, 0)),
            getBottomRight(finalPlaceTopLeft).sum(Vector2D.of(-PIXEL_OFFSET, 0)),
            finalPlaceTopLeft.sum(Vector2D.of(PIXEL_OFFSET, 0)),
            getTopRight(finalPlaceTopLeft).sum(Vector2D.of(-PIXEL_OFFSET, 0))
        );

        return generalCanAccess(
            coordinates,
            capabilities, currentEntityGameGridCoordinate,
            Maybe.just(moveDirection));
    }

    private boolean generalCanAccess(Stream<Vector2D> coordinatesToCheck,
                                     GameCharacterCapabilities capabilities,
                                     Vector2D currentEntityGameGridCoordinate,
                                     Maybe<Direction> moveDirection)
    {
        return coordinatesToCheck.allMatch(c ->
            checkCoordinate(c, capabilities, currentEntityGameGridCoordinate, moveDirection));
    }

    public boolean checkCoordinate(Vector2D coordinate,
                                   GameCharacterCapabilities capabilities,
                                   Vector2D currentEntityGameGridCoordinate,
                                   Maybe<Direction> moveDirection)
    {
        Vector2D gridCoordinate = gameGrid.modelCoord2GridCoord(coordinate);

        if (isOnAliveWall(gridCoordinate) && !capabilities.goesThroughWalls())
            return false;

        if (isOnBlock(gridCoordinate))
            return false;

        if (isOnPowerUp(gridCoordinate) && !capabilities.takesPowerUps())
            return false;

        if (!(capabilities instanceof BomberManCapabilities bomberManCapabilities &&
            bomberManCapabilities.goesThroughBombs()))
        {
            Maybe<Direction> directionTowardsBomb =
                Direction.fromDirectionCoordinate(gridCoordinate.sub(currentEntityGameGridCoordinate));

            if (directionTowardsBomb.isJust() && moveDirection.isJust() &&
                directionTowardsBomb.unwrap().equals(moveDirection.unwrap()) &&
                isOnBomb(gridCoordinate) && !isOnBomb(currentEntityGameGridCoordinate))
                return false;
        }

        return true;
    }

    private static final int PIXEL_OFFSET = 15;

    private Vector2D getTopRightSmall(Vector2D coordinateTopLeft)
    {
        return getTopRight(coordinateTopLeft).sum(Vector2D.of(-PIXEL_OFFSET, PIXEL_OFFSET));
    }

    private Vector2D getBottomRightSmall(Vector2D coordinateTopLeft)
    {
        return getBottomRight(coordinateTopLeft).sum(Vector2D.of(-PIXEL_OFFSET, -PIXEL_OFFSET));
    }

    private Vector2D getBottomLeftSmall(Vector2D coordinateTopLeft)
    {
        return getBottomLeft(coordinateTopLeft).sum(Vector2D.of(PIXEL_OFFSET, -PIXEL_OFFSET));
    }

    private Vector2D smallify(Vector2D coordinate)
    {
        return coordinate.sum(Vector2D.of(PIXEL_OFFSET, PIXEL_OFFSET));
    }


    public boolean isOnBlock(Vector2D gridCoordinate)
    {
        return gameGrid.get(gridCoordinate)
            .getEntitiesOn()
            .stream()
            .anyMatch(entity -> entity instanceof Block);
    }

    public boolean isOnBomb(Vector2D gridCoordinate)
    {
        return gameGrid.get(gridCoordinate)
            .getEntitiesOn()
            .stream()
            .anyMatch(entity -> entity instanceof Bomb);
    }

    public boolean isOnAliveWall(Vector2D gridCoordinate)
    {
        return gameGrid.get(gridCoordinate)
            .getEntitiesOn()
            .stream()
            .anyMatch(entity -> entity instanceof Wall && !((Wall) entity).isDisappearing());
    }

    private boolean isOnPowerUp(Vector2D gridCoordinate)
    {
        return gameGrid.get(gridCoordinate)
            .getEntitiesOn()
            .stream()
            .anyMatch(entity -> entity instanceof Powerup);
    }

    @Deprecated(forRemoval = true)
    public boolean isInCorner(Vector2D gridCoordinate)
    {
        int x = gridCoordinate.getX();
        int y = gridCoordinate.getY();
        return (y == 1 || y == gameGrid.getHeight() - 2) &&
            (x == 1 || x == gameGrid.getWidth() - 2);
    }

    @Deprecated(forRemoval = true)
    public boolean IsAlongSides(Vector2D gridCoordinate)
    {
        int x = gridCoordinate.getX();
        int y = gridCoordinate.getY();

        return y == 1 || y == gameGrid.getHeight() - 2 ||
            x == 1 || x == gameGrid.getWidth() - 2;
    }

    /**
     * @param topLeft topLeftCoordinate of a GameCharacter
     * @return true if the bomberManCharacter could move relatively to blocks
     * in at least 3 directions
     */
    public boolean isAtCrossroad(Vector2D topLeft)
    {
        Vector2D normalizedCoordinate =
            topLeft.sub(gameGrid.getTopLeftCornerModelCoordinate());

        // Check if we are aligned with the rows and columns in the grid
        if (!normalizedCoordinate.mod(Entity.SIZE_IN_MODEL).isZero())
            return false;

        Vector2D gridCoordinate = gameGrid.modelCoord2GridCoord(topLeft);

        // Check that we are in odd rows and columns
        return gridCoordinate.getY() % 2 == 1 && gridCoordinate.getX() % 2 == 1;
    }
}
