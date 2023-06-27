package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.GamePlayException;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.Enemy;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.Wall;

/**
 * SimpleDeadState is the DeadState of entities whose death is just a fading
 *
 * @param <T> the specific type of the Stateful
 * @author gp
 */
public class SimpleDeadState<T extends Entity & StateFul> extends DeadState
{
	/**
	 * deathImageManager is the ImageManager that will display the images
	 */
	private final ImageManager deathImageManager;

	/**
	 * public constructor of SimpleDeadState from the Stateful entity and
	 * ImageManager
	 *
	 * @param entity            the Entity whose state is this
	 * @param deathImageManager the ImageManager that will handle the death
	 *                          sequence
	 */
	public SimpleDeadState(T entity, ImageManager deathImageManager)
	{
		super(entity);
		this.deathImageManager = deathImageManager;
	}

	/**
	 * getDrawing() gets the image from the ImageManager
	 *
	 * @return the image representing the dying enemy
	 */
	@Override
	public Drawing getDrawing()
	{
		AbstractImage image = deathImageManager.getImage();
		if (deathImageManager.hasChanged())
			getEntity().hasChanged();
		return getEntity().getDrawing(image);
	}

	/**
	 * updateInModel() removes the Entity from the Grid if the ImageManager has
	 * done one cycle
	 */
	@Override
	public void updateInModel()
	{
		Entity entity = getEntity();

		if (getDeathImageManager().hasDoneOneCycle()) {
			GameGrid gameGrid = entity.getGameGrid();

			if (entity instanceof Wall wall)
				gameGrid.get(
					gameGrid.getCenterInGridCoord(entity)
				).removeWall(wall);
			else if (entity instanceof Enemy enemy)
				gameGrid.get(
					gameGrid.getCenterInGridCoord(enemy)
				).removeCharacter(enemy);
			else
				throw new GamePlayException(
					"What is this. What should I do with" + entity.getClass());
		}
	}

	/**
	 * getEntity() gets the underlying Stateful casting it to an Entity
	 *
	 * @return the Entity whose state is this
	 */
	protected Entity getEntity()
	{
		return (Entity) super.getStateful();
	}

	/**
	 * getDeathImageManager() must be used to get the ImageManager from subclasses
	 *
	 * @return the ImageManager
	 */
	protected ImageManager getDeathImageManager()
	{
		return deathImageManager;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
