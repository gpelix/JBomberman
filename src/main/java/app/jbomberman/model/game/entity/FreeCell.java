package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.GamePlayException;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * FreeCell is a Cell that can contain other entities
 *
 * @author gp
 */
public class FreeCell extends Cell
{
	/**
	 * GREEN_BACKGROUND is the path to the Image to show for FreeCells
	 */
	private static final String GREEN_BACKGROUND = "GreenBackground.png";

	/**
	 * entities is the set of entities currently on the FreeCell
	 */
	private final Set<Entity> entities;

	/**
	 * powerup contains a reference to a powerup if there is any on the FreeCell
	 */
	private Maybe<Powerup> powerup = Maybe.nothing();

	/**
	 * wall contains a reference to a wall if there is any on the FreeCell
	 */
	private Maybe<Wall> wall = Maybe.nothing();

	/**
	 * bomb contains a reference to a bomb if there is any on the FreeCell
	 */
	private Maybe<Bomb> bomb = Maybe.nothing();

	/**
	 * door contains a reference to a door if there is any on the FreeCell
	 */
	private Maybe<Door> door = Maybe.nothing();

	/**
	 * fire contains a reference to a fire if there is any on the FreeCell
	 */
	private Maybe<Fire> fire = Maybe.nothing();

	/**
	 * BomberMan contains a reference to bomberman if it's on this FreeCell
	 */
	private Maybe<BomberMan> bomberMan = Maybe.nothing();

	/**
	 * public constructor of FreeCell from its model-coordinate, the GameGrid to which it
	 * is attached and its AbstractImage
	 *
	 * @param coordinate the model-coordinate of the Cell
	 * @param gameGrid   the GameGrid to which the Cell is attached
	 */
	public FreeCell(Vector2D coordinate, GameGrid gameGrid)
	{
		super(coordinate,
			FlightWeightImagesFactory.getInstance().getImage(GREEN_BACKGROUND),
			gameGrid);
		entities = new HashSet<>();
	}

	/**
	 * addGameCharacter() adds a GameCharacter to the FreeCell
	 *
	 * @param gameCharacter the GameCharacter to add to the FreeCell
	 */
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

	/**
	 * addPowerUp() adds a Powerup to the FreeCell
	 *
	 * @param powerup the Powerup to add to the FreeCell
	 */
	public void addPowerUp(Powerup powerup)
	{
		this.powerup = Maybe.just(powerup);

		// This is not possible
		if (fire.isJust())
			powerup.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

		entities.add(powerup);
	}

	/**
	 * addWall() adds a wall to the FreeCell
	 *
	 * @param wall the Wall to add to the FreeCell
	 */
	public void addWall(Wall wall)
	{
		this.wall = Maybe.just(wall);
		// this is not possible
		if (fire.isJust())
			wall.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

		entities.add(wall);
	}

	/**
	 * addBlock() adds a Block to the FreeCell
	 *
	 * @param block the Block to add to the FreeCell
	 */
	public void addBlock(Block block)
	{
		entities.add(block);
	}

	/**
	 * addBomb() adds a Bomb to the FreeCell
	 *
	 * @param bomb the Bomb to add to the FreeCell
	 */
	public void addBomb(Bomb bomb)
	{
		this.bomb = Maybe.just(bomb);

		if (fire.isJust())
			bomb.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

		entities.add(bomb);
	}

	/**
	 * addDoor() adds a Door to the FreeCell
	 *
	 * @param door the Bomb to add to the FreeCell
	 */
	public void addDoor(Door door)
	{
		this.door = Maybe.just(door);

		// This is not possible
		if (fire.isJust())
			door.notifyExplosion(new Explosion(0, getCenterInGridCoord()));

		entities.add(door);
	}

	/**
	 * addFire() adds a Fire to the FreeCell
	 *
	 * @param fire the Fire to add to the FreeCell
	 */
	public void addFire(Fire fire)
	{
		this.fire = Maybe.just(fire);

		entities.add(fire);
	}

	/**
	 * removeBomb() removes the Bomb from the FreeCell
	 *
	 * @param entity the bomb to remove
	 * @throws GamePlayException if there is no Bomb on the FreeCell or if the
	 *                           bomb on the FreeCell is different from the one
	 *                           specified in input
	 */
	public void removeBomb(Bomb entity)
	{
		if (bomb.unwrapOrThrow(
			() -> new GamePlayException(
				"Removing a Bomb from a freeCell not containing a bomb. Weired!"
			)) != entity)
			throw new GamePlayException("Wrong bomb");
		bomb = Maybe.nothing();
		entities.remove(entity);
	}

	/**
	 * removeWall() removes the Wall from the FreeCell
	 *
	 * @param entity the wall to remove
	 * @throws GamePlayException if there is no Wall on the FreeCell or if the
	 *                           wall on the FreeCell is different from the one
	 *                           specified in input
	 */
	public void removeWall(Wall entity)
	{
		if (wall.unwrapOrThrow(() -> new GamePlayException(
			"Removing a Wall from a freeCell not containing walls. Weird!"))
			!= entity)
			throw new GamePlayException("Wrong Wall");
		wall = Maybe.nothing();
		entities.remove(entity);
	}

	/**
	 * removeFire() removes the Fire from the FreeCell
	 *
	 * @param entity the fire to remove
	 * @throws GamePlayException if there is no Fire on the FreeCell or if the
	 *                           fire on the FreeCell is different from the one
	 *                           specified in input
	 */
	public void removeFire(Fire entity)
	{
		if (fire.unwrapOrThrow(() -> new GamePlayException(
			"Removing a Fire from a FreeCell not containing fire. Weird!"
		)) != entity)
			throw new GamePlayException("Wrong fire to remove");

		fire = Maybe.nothing();
		entities.remove(entity);
	}

	/**
	 * removePowerup() removes the Powerup from the FreeCell
	 *
	 * @param entity the powerup to remove
	 * @throws GamePlayException if there is no Powerup on the FreeCell or if the
	 *                           powerup on the FreeCell is different from the one
	 *                           specified in input
	 */
	public void removePowerup(Powerup entity)
	{
		if (powerup.unwrapOrThrow(() -> new JBomberManException(
			"Removing a Powerup from a freeCell not containing powerups. " +
				"Weird!")) != entity)
			throw new GamePlayException("Wrong Powerup to remove");

		powerup = Maybe.nothing();
		entities.remove(entity);
	}

	/**
	 * removeGameCharacter() removes the GameCharacter from the FreeCell
	 *
	 * @param character the GameCharacter to remove
	 * @throws GamePlayException if there is no GameCharacter on the FreeCell or
	 *                           if the GameCharacter on the FreeCell is different
	 *                           from the one specified in input
	 */
	public void removeCharacter(GameCharacter character)
	{
		if (character instanceof BomberMan)
			bomberMan = Maybe.nothing();
		entities.remove(character);
	}

	/**
	 * contains() checks whether the FreeCell contains the entity specified in
	 * input
	 *
	 * @param entity the entity whose presence must be verified
	 * @return whether the FreeCell contains the entity
	 */
	public boolean contains(Entity entity)
	{
		return entities.contains(entity);
	}

	/**
	 * getEntitiesOn() gets the set of entities on the FreeCell
	 *
	 * @return the set of entities on the FreeCell
	 */
	public Set<Entity> getEntitiesOn()
	{
		return entities;
	}

	/**
	 * getDrawing() gets the Drawing of the FreeCell and also includes the ones of
	 * all the entities which are placed on this FreeCell
	 *
	 * @return the Drawing of the FreeCell with all the Entities contained in this
	 * FreeCell on top of it
	 */
	@Override
	public Drawing getDrawing()
	{
		Drawing ret = new Drawing(super.getDrawing());

		entities.forEach(entity -> ret.merge(entity.getDrawing(),
			x -> x + 1));

		return ret;
	}

	/**
	 * updateInModel() calls updateInModel() on every entity in the FreeCell
	 */
	@Override
	public void updateInModel()
	{
		// Copying the starting set is necessary as updateInModel called on
		// entities might update entities and throw java.util.ConcurrentModificationException
		(new ArrayList<>(entities)).forEach(Entity::updateInModel);
	}

	/**
	 * notifyExplosion() handles the explosion of a Bomb.
	 * It forwards the Explosion to the Entites in the FreeCell bearing in mind
	 * that Walls and Bombs must be notified before any other Entity
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return whether the Explosion has been stopped or not
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		if (explosion.getStrength() == 0)
			return false;

		// If there is a Wall or a Bomb we handle it before propagating the event
		// to other Entities
		if (wall.compute(false, w -> w.notifyExplosion(explosion)) ||
			bomb.compute(false, b -> b.notifyExplosion(explosion)))
			return true;

		for (Entity entity : new HashSet<>(entities))
			if (entity.notifyExplosion(explosion))
				return true;

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
					case SOUTH: // FALLTHROUGH
					case NORTH:
						yield Fire.FireType.VERTICAL;
					case WEST: // FALLTHROUGH
					case EAST:
						yield Fire.FireType.HORIZONTAL;
					default:
						throw new GamePlayException("Shouldn't be here");
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

	@Override
	public boolean allowsBombs()
	{
		return entities.stream().allMatch(Entity::allowsBombs);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
