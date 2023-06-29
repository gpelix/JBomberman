package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.LayeredDrawObject;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.MutableVector2D;
import app.jbomberman.util.Vector2D;

/**
 * Entity is the superclass for whatever class that models an object that can be
 * placed in the GameGrid
 *
 * @author gp
 */
public abstract class Entity extends MutableVector2D
{
	/**
	 * SIZE_IN_MODEL is the size in model coordinates of the square of the entity
	 */
	public static final int SIZE_IN_MODEL = 16 * Properties.getInstance().getScaleFactor();

	/**
	 * GameGrid is the grid to which the entity has been added
	 */
	private final GameGrid gameGrid;

	/**
	 * oldDrawing is the last drawing of the Entity.
	 * Many entities don't really need constant updates, so it's useful to keep a
	 * cached Drawing
	 */
	private Maybe<Drawing> oldDrawing;

	/**
	 * protected constructor must be called by subclasses to initialize the Entity
	 * from a coordinate (that is the model-coordinate) and the gameGrid
	 *
	 * @param coordinate the model-coordinate of the Entity
	 * @param gameGrid   the GameGrid to which the Entity is attached
	 */
	protected Entity(Vector2D coordinate, GameGrid gameGrid)
	{
		super(coordinate);
		this.gameGrid = gameGrid;
		oldDrawing = Maybe.nothing();
	}

	/**
	 * Utility function meant to be used by subclasses to create a drawing from
	 * an image.
	 * If there is the old drawing that is Just, that is returned without doing
	 * anything else.
	 * For this function to do something hasChanged() must be called before
	 *
	 * @param img AbstractImage to draw at the coordinates specified by the
	 *            coordinates of this Entity
	 * @return the drawing of the Entity based on the image provided in input if
	 * the object hasChanged otherwise the old drawing
	 */
	public Drawing getDrawing(AbstractImage img)
	{
		if (oldDrawing.isJust())
			return oldDrawing.fromJust();

		Drawing newDrawing = new Drawing();

		newDrawing.add(new LayeredDrawObject(this, img,
			EntityHierarchy.getInstance().getDrawLevelFor(this).unwrapOrThrow(() ->
				new JBomberManException("Don't know where to place " + this.getClass().getName()))));

		oldDrawing = Maybe.just(newDrawing);

		return newDrawing;
	}

	/**
	 * hasChanged() sets the old drawing to nothing
	 */
	public void hasChanged()
	{
		oldDrawing = Maybe.nothing();
	}

	/**
	 * getDrawing() should be overwritten in subclasses and must contain the logic to get
	 * a drawing out of the Entity
	 *
	 * @return the Drawing of the Entity
	 */
	public abstract Drawing getDrawing();

	/**
	 * updateInModel() must be called to update the state of the Entity.
	 * This function should be called repeatedly by the Game class
	 */
	public abstract void updateInModel();

	/**
	 * notifyExplosion() must be called to notify that an explosion has happened
	 * and must handle it.
	 * If the Entity overwriting notifyExplosion() blocks the explosion,
	 * notifyExplosion() must return true
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return whether the explosion was stopped
	 */
	public abstract boolean notifyExplosion(Explosion explosion);

	/**
	 * toString() is only used for debugging purpose
	 *
	 * @return the String representation of the Entity
	 */
	@Override
	public String toString()
	{
		return "Entity(MutableVector2D=" + super.toString() + ")";
	}

	/**
	 * getGameGrid() get the gameGrid
	 *
	 * @return the gameGrid
	 */
	public GameGrid getGameGrid()
	{
		return gameGrid;
	}

	/**
	 * getUnderlyingFreeCell() gets the underlying FreeCell
	 *
	 * @return the underlying FreeCell
	 */
	public FreeCell getUnderlyingFreeCell()
	{
		return gameGrid.get(getCenterInGridCoord());
	}

	/**
	 * getCenterInGridCoord() gets the grid coordinate of the underlying FreeCell
	 *
	 * @return the grid coordinate of the underlying FreeCell
	 */
	public Vector2D getCenterInGridCoord()
	{
		return gameGrid.getCenterInGridCoord(this);
	}

	/**
	 * allowsBombs() is a function meant to be overwritten by subclasses that
	 * returns true if and only if it's possible to place a bomb on the Entity
	 *
	 * @return whether it's possible to place a bomb on the Entity
	 */
	public abstract boolean allowsBombs();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
