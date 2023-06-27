package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.model.game.entity.Wall;

/**
 * WallAliveState is the alive state of Walls
 *
 * @author gp
 */
public class WallAliveState extends AliveState
{
	/**
	 * wall is a reference to the Wall that has this as state
	 */
	private final Wall wall;

	/**
	 * imageManager is wall's ImageManager
	 */
	private final ImageManager imageManager;

	/**
	 * public constructor of WallAliveState
	 *
	 * @param wall         the Wall that has this as state
	 * @param imageManager the ImageManager of the Wall
	 * @param nextState    the state to set when the Wall dies
	 */
	public WallAliveState(Wall wall, AbstractState nextState,
												ImageManager imageManager)
	{
		super(wall, nextState);
		this.wall = wall;
		this.imageManager = imageManager;
	}

	/**
	 * notifyExplosion() must be called to notify the Wall that there has been
	 * an explosion and the Wall gets destroyed
	 *
	 * @param explosion the Explosion object
	 * @return true as Walls block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		die();
		return true;
	}

	/**
	 * updateInModel() does nothing as Walls do not require any update
	 */
	@Override
	public void updateInModel()
	{
		// Do nothing
	}

	/**
	 * getDrawing() simply gets the only image in the ImageManager
	 *
	 * @return the Image of the Wall
	 */
	@Override
	public Drawing getDrawing()
	{
		return wall.getDrawing(imageManager.getImage());
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
