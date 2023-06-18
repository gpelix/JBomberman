package app.jbomberman.model.game.entity;

import app.jbomberman.util.Maybe;

import java.util.HashMap;
import java.util.Map;

public class EntityHierarchy
{
    private final Map<Class<? extends Entity>, Integer> drawingHierarchy = new HashMap<>()
    {
        {
            put(BomberMan.class, 6);
            put(Enemy.class, 5);
            put(Bomb.class, 4);
            put(Wall.class, 4);
            put(Fire.class, 4);
            put(Block.class, 3);
            put(Door.class, 2);
            put(Powerup.class, 2);
            put(FreeCell.class, 1);
        }
    };

    private final Map<Class<? extends Entity>, Integer> updateHierarchy = new HashMap<>()
    {
        {
            put(Wall.class, 60);
            put(Block.class, 60);
            put(Bomb.class, 60);
            put(PickableCell.class, 50);
            put(Fire.class, 40);
            put(BomberMan.class, 30);
            put(Enemy.class, 30);
            put(FreeCell.class, 20);
        }
    };

    private static EntityHierarchy instance;

    public static EntityHierarchy getInstance()
    {
        if (instance == null)
            instance = new EntityHierarchy();

        return instance;
    }


    public Maybe<Integer> getUpdateLevelFor(Entity e)
    {
        return getAssociation(updateHierarchy, e);
    }

    public Maybe<Integer> getDrawLevelFor(Entity e)
    {
        return getAssociation(drawingHierarchy, e);
    }

    public Maybe<Integer> getAssociation(Map<Class<? extends Entity>, Integer> classMap, Entity e)
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
