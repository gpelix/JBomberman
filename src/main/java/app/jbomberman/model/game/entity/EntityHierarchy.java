package app.jbomberman.model.game.entity;

import app.jbomberman.util.Maybe;

import java.util.Map;

/**
 * EntityHierarchy is a singleton modelling the update hierarchy of Entities
 *
 * @author gp
 */
public class EntityHierarchy
{
	/**
	 * drawingHierarchy is the Hierarchy that associates with each class an
	 * integer. Lower is the integer, and sooner it will be drawn
	 */
	private final Map<Class<? extends Entity>, Integer> drawingHierarchy = Map.of(
		BomberMan.class, 6,
		Enemy.class, 5,
		Bomb.class, 4,
		Wall.class, 4,
		Fire.class, 4,
		Block.class, 3,
		Door.class, 2,
		Powerup.class, 2,
		FreeCell.class, 1
	);

	/**
	 * instance keeps a reference to the unique instance of the EntityHierarchy
	 */
	private static EntityHierarchy instance;

	/**
	 * getInstance() must be used to get the unique instance of the EntityHierarchy
	 *
	 * @return the unique instance of the EntityHierarchy
	 */
	public static EntityHierarchy getInstance()
	{
		if (instance == null)
			instance = new EntityHierarchy();

		return instance;
	}

	/**
	 * private empty constructor to forbid initialization the EntityHierarchy from
	 * outside this class
	 */
	private EntityHierarchy()
	{
	}

	/**
	 * getDrawLevelFor() gets the Maybe Integer associated with the Entity's class
	 *
	 * @param e the Entity
	 * @return the Integer that represents the priority of drawing
	 */
	public Maybe<Integer> getDrawLevelFor(Entity e)
	{
		return getAssociation(drawingHierarchy, e);
	}

	/**
	 * getAssociation() gets must be used internally to get the priority of
	 * Drawing for the Entity
	 *
	 * @param classMap the Map
	 * @param e        the Entity whose priority must get
	 * @return the priority of Drawing of the Entity e
	 */
	private Maybe<Integer> getAssociation(
		Map<Class<? extends Entity>, Integer> classMap,
		Entity e)
	{
		Class<?> classOfE = e.getClass();

		if (classMap.containsKey(classOfE))
			return Maybe.just(classMap.get(classOfE));

		for (Map.Entry<Class<? extends Entity>, Integer> entry : classMap.entrySet()) {
			Class<?> cEntry = entry.getKey();
			if (cEntry.isInstance(e))
				return Maybe.just(entry.getValue());
		}

		return Maybe.nothing();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
