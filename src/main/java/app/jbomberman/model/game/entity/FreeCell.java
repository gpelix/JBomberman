package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.entity.gamecharacter.BomberMan;
import app.jbomberman.model.game.entity.gamecharacter.Enemy;
import app.jbomberman.util.Direction;
import app.jbomberman.util.FunctionalUtils;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FreeCell extends Cell
{
    private static final String GREEN_BACKGROUND = "GreenBackground.png";

    private final Set<Entity> entities;

    private Maybe<Powerup> powerup = Maybe.nothing();

    private Maybe<Wall> wall = Maybe.nothing();

    private Maybe<Bomb> bomb = Maybe.nothing();

    private Maybe<Door> door = Maybe.nothing();

    private Maybe<Fire> fire = Maybe.nothing();

    private Maybe<BomberMan> bomberMan = Maybe.nothing();

    public FreeCell(Vector2D coordinate, GameGrid gameGrid)
    {
        super(coordinate,
            FlightWeightImagesFactory.getInstance().getImage(GREEN_BACKGROUND),
            gameGrid);
        entities = new HashSet<>();
    }

    /**
     * addEntityInFreeCell adds the entity to entities that are currently on this freecell
     *
     * @param entity entity to add
     */
    @Deprecated(forRemoval = true)
    public void addEntity(Entity entity)
    {
        if (entity instanceof Powerup powerupUnwrapped)
            powerup = Maybe.just(powerupUnwrapped);
        else if (entity instanceof Wall wallUnwrapped)
            wall = Maybe.just(wallUnwrapped);
        else if (entity instanceof BomberMan bomberManU) {
            bomberMan = Maybe.just(bomberManU);
            if (wall.isNothing() || wall.unwrap().isDisappearing()) {
                if (powerup.isJust())
                    bomberManU.takePowerup(powerup.unwrap());
                door.maybeDo(d -> d.pick(bomberManU));
            }
            if (getEntitiesOn().stream()
                .anyMatch(e -> e instanceof Enemy && !((Enemy) e).isDisappearing()))
                bomberManU.caughtByEnemy();
        } else if (entity instanceof Bomb bombU)
            bomb = Maybe.just(bombU);
        else if (entity instanceof Door doorU)
            door = Maybe.just(doorU);
        else if (entity instanceof Fire fireU)
            fire = Maybe.just(fireU);
        else if (entity instanceof Enemy && bomberMan.isJust()) {
            bomberMan.unwrap().caughtByEnemy();
        }

        // This is just a trick
        // FIXME:
        if (fire.isJust())
            entity.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

        entities.add(entity);
    }

    @Deprecated(forRemoval = true)
    public void remove(Entity entity)
    {
        if (entity instanceof Powerup) {
            powerup.isJustOrThrow(() -> new JBomberManException(
                "Removing a Powerup from a freeCell not containing powerups. " +
                    "Weird!"));
            powerup = Maybe.nothing();
        } else if (entity instanceof Wall) {
            wall.isJustOrThrow(() -> new JBomberManException(
                "Removing a Wall from a freeCell not containing walls. " +
                    "Weird!"));
            wall = Maybe.nothing();
        } else if (entity instanceof Bomb) {
            bomb.isJustOrThrow(() -> new JBomberManException(
                "Removing a Bomb from a freeCell not containing a bomb. Weired!"
            ));
            bomb = Maybe.nothing();
        } else if (entity instanceof Fire)
            fire = Maybe.nothing();
        else if (entity instanceof BomberMan)
            bomberMan = Maybe.nothing();

        entities.remove(entity);
    }

    public boolean contains(Entity entity)
    {
        return entities.contains(entity);
    }

    public Set<Entity> getEntitiesOn()
    {
        return entities;
    }

    @Override
    public Drawing getDrawing()
    {
        Drawing ret = super.getDrawing();

        entities.forEach(entity -> ret.merge(entity.getDrawing(),
            FunctionalUtils::inc));

        return ret;
    }

    @Override
    public void updateInModel()
    {

        // Copying the starting set is necessary as updateInModel called on
        // entities might update entities and throw java.util.ConcurrentModificationException

        (new HashSet<>(entities)).forEach(Entity::updateInModel);
    }

    @Override
    public boolean notifyExplosion(Explosion explosion)
    {
        if (explosion.getStrength() == 0)
            return false;

        // What I'm doing is really a horror
        if (wall.isJust())
            if (wall.unwrap().notifyExplosion(explosion))
                return true;
        if (bomb.isJust())
            if (bomb.unwrap().notifyExplosion(explosion))
                return true;

        for (Entity entity : new HashSet<>(entities)) {
            if (entity.notifyExplosion(explosion)) {
                return true;
            }
        }

        GameGrid gameGrid = getGameGrid();
        Vector2D gridCoordinate = gameGrid.modelCoord2GridCoord(this);

        Explosion newExplosion = new Explosion(explosion.getStrength() - 1,
            getCenterInGridCoord());
        // If this freecell is the source of the explosion (that is the
        // fromGridCoordinate is this) it must
        // propagate it in all the dimensions otherwise it must only go
        // forward
        Vector2D deltaSfromPrevious = gridCoordinate.sub(explosion.getFromGridCoordinate());

        Maybe<Direction> direction = Direction.fromDirectionCoordinate(
            deltaSfromPrevious);

        if (direction.isNothing()) {
            List<Vector2D> neighbors = gameGrid.getNeighborsCoordinates(getCenterInGridCoord());

            neighbors.forEach(coordinate -> gameGrid.get(coordinate).notifyExplosion(newExplosion));

            // Add center of the explosion
            gameGrid.addEntityInFreeCell(
                EntityFactory.getInstance().buildFire(Fire.FireType.CENTER, this, gameGrid),
                getCenterInGridCoord()
            );
        } else {
            Direction directionUnwrapped = direction.unwrap();

            Vector2D freeCellToNotify = gridCoordinate.sum(deltaSfromPrevious);

            Fire.FireType typeOfFire = switch (directionUnwrapped) {
                case EAST -> Fire.FireType.EAST;
                case WEST -> Fire.FireType.WEST;
                case SOUTH -> Fire.FireType.SOUTH;
                case NORTH -> Fire.FireType.NORTH;
            };

            if (explosion.getStrength() > 1) {
                typeOfFire = switch (typeOfFire) {
                    case SOUTH:
                    case NORTH:
                        yield Fire.FireType.VERTICAL;
                    case WEST:
                    case EAST:
                        yield Fire.FireType.HORIZONTAL;
                    default:
                        throw new JBomberManException("Shouldn't be here");
                };
            }

            gameGrid.addEntityInFreeCell(
                EntityFactory.getInstance().buildFire(typeOfFire, this, gameGrid),
                getCenterInGridCoord()
            );

            gameGrid.get(freeCellToNotify).notifyExplosion(newExplosion);
        }
        return false;
    }

    public boolean hasBomb()
    {
        return bomb.isJust();
    }

    public boolean hasWall()
    {
        return wall.isJust();
    }
}