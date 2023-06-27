package app.jbomberman.model.game.entity.state;

import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Wall;

/**
 * WallDeadState is the state of a Wall when it has been bit by a Bomb
 *
 * @author gp
 */
public class WallDeadState extends SimpleDeadState<Wall>
{
	/**
	 * public constructor of WallDeadState
	 *
	 * @param entity            the Wall whose state is this
	 * @param deathImageManager the ImageManager that will handle the death
	 *                          sequence
	 */
	public WallDeadState(Wall entity,
											 ImageManager deathImageManager)
	{
		super(entity, deathImageManager);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
