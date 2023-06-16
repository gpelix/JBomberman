package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

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

    public void addGameCharacter(GameCharacter gameCharacter)
    {
        if (gameCharacter instanceof Enemy && bomberMan.isJust())
            bomberMan.fromJust().caughtByEnemy();
        else if (gameCharacter instanceof BomberMan bomberManU) {
            bomberMan = Maybe.just(bomberManU);
            if (wall.isNothing() || wall.fromJust().isDisappearing()) {
                if (powerup.isJust())
                    bomberManU.takePowerup(powerup.fromJust());
                door.maybeDo(d -> d.pick(bomberManU));
            }
            if (getEntitiesOn().stream()
                .anyMatch(e -> e instanceof Enemy && !((Enemy) e).isDisappearing()))
                bomberManU.caughtByEnemy();
        }
        if (fire.isJust())
            gameCharacter.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

        entities.add(gameCharacter);
    }

    public void addPowerUp(Powerup powerup)
    {
        this.powerup = Maybe.just(powerup);

        // This is not possible
        if (fire.isJust())
            powerup.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

        entities.add(powerup);
    }

    public void addWall(Wall wall)
    {
        this.wall = Maybe.just(wall);
        // this is not possible
        if (fire.isJust())
            wall.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

        entities.add(wall);
    }

    public void addBlock(Block block)
    {
        entities.add(block);
    }

    public void addBomb(Bomb bomb)
    {
        this.bomb = Maybe.just(bomb);

        if (fire.isJust())
            bomb.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

        entities.add(bomb);
    }

    public void addDoor(Door door)
    {
        this.door = Maybe.just(door);

        // This is not possible
        if (fire.isJust())
            door.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

        entities.add(door);
    }

    public void addFire(Fire fire)
    {
        this.fire = Maybe.just(fire);

        entities.add(fire);
    }

    public void removeBomb(Bomb entity)
    {
        bomb.isJustOrThrow(() -> new JBomberManException(
            "Removing a Bomb from a freeCell not containing a bomb. Weired!"
        ));
        bomb = Maybe.nothing();
        entities.remove(entity);
    }

    public void removeWall(Wall entity)
    {
        wall.isJustOrThrow(() -> new JBomberManException(
            "Removing a Wall from a freeCell not containing walls. " +
                "Weird!"));
        wall = Maybe.nothing();
        entities.remove(entity);
    }

    public void removeFire(Fire entity)
    {
        // Should we do more??
        fire = Maybe.nothing();
        entities.remove(entity);

    }

    public void removePowerup(Powerup entity)
    {
        powerup.isJustOrThrow(() -> new JBomberManException(
            "Removing a Powerup from a freeCell not containing powerups. " +
                "Weird!"));
        powerup = Maybe.nothing();

        entities.remove(entity);
    }

    public void removeCharacter(GameCharacter character)
    {
        if (character instanceof BomberMan)
            bomberMan = Maybe.nothing();
        entities.remove(character);
    }

    @Deprecated(forRemoval = true)
    public void _remove(Entity entity)
    {
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
            x -> x + 1));

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
            if (wall.fromJust().notifyExplosion(explosion))
                return true;
        if (bomb.isJust())
            if (bomb.fromJust().notifyExplosion(explosion))
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

        Maybe<Direction> direction = Direction.fromDirectionVector(
            deltaSfromPrevious);

        if (direction.isNothing()) {
            Stream<Vector2D> neighbors = gameGrid.getNeighborsCoordinates(getCenterInGridCoord());

            neighbors.forEach(coordinate -> gameGrid.get(coordinate).notifyExplosion(newExplosion));

            // Add center of the explosion
            gameGrid.addFire(
                EntityFactory.getInstance().buildFire(Fire.FireType.CENTER, this, gameGrid),
                getCenterInGridCoord()
            );
        } else {
            Direction directionUnwrapped = direction.fromJust();

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

            gameGrid.addFire(
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