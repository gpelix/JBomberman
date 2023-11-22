/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
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
