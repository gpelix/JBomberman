package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.LayeredDrawObject;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.MutableVector2D;
import app.jbomberman.util.Vector2D;

/**
 * Entity is the superclass of whatever can be drawn on the screen during
 * the gameplay
 *
 * @author gp
 */
public abstract class Entity extends MutableVector2D
{
	public static final int SIZE_IN_MODEL = 16 * Properties.getInstance().getScaleFactor();

	private final GameGrid gameGrid;

	private Maybe<Drawing> oldDrawing;


	public Entity(Vector2D coordinate, GameGrid gameGrid)
	{
		super(coordinate);
		this.gameGrid = gameGrid;
		oldDrawing = Maybe.nothing();
	}

	/**
	 * Utility function meant to be used by subclasses to draw a specific image.
	 * This function doesn't do anything unless the hasChanged() function is called
	 *
	 * @param img ImageView to draw at the coordinates specified by the coordinates
	 *            of this Entity
	 */
	public Drawing getDrawing(AbstractImage img)
	{
		if (oldDrawing.isJust())
			return oldDrawing.unwrap();

		Drawing newDrawing = new Drawing();

		newDrawing.add(new LayeredDrawObject(this, img, EntityHierarchy.getInstance().getDrawLevelFor(this).unwrapOrThrow(() ->
			new JBomberManException("Don't know where to place " + this.getClass().getName()))));

		oldDrawing = Maybe.just(newDrawing);

		return newDrawing;
	}

	public void hasChanged()
	{
		oldDrawing = Maybe.nothing();
	}

	/**
	 * draw should be used to decide which image to print
	 */
	public abstract Drawing getDrawing();

	public abstract void updateInModel();

	// boolean is because we need to know if the explosion was stopped
	public abstract boolean notifyExplosion(Explosion explosion);

	@Override
	public String toString()
	{
		return "Entity(MutableVector2D=" + super.toString() + ")";
	}

	public final GameGrid getGameGrid()
	{
		return gameGrid;
	}

	public final FreeCell getUnderlyingFreeCell()
	{
		return gameGrid.get(getCenterInGridCoord());
	}

	public final Vector2D getCenterInGridCoord()
	{
		return gameGrid.getCenterInGridCoord(this);
	}

	public abstract boolean allowsBombs();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
