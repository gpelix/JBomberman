package app.jbomberman.model.game.entity;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

/**
 * Cell is a class the models an Entity that does never change its state
 *
 * @author gp
 */
public abstract class Cell extends Entity
{
	/**
	 * img is the AbstractImage of the Cell
	 */
	private final AbstractImage img;

	/**
	 * protected constructor meant to be called by subclasses' constructors to
	 * initialize the Cell from its model-coordinate, the GameGrid to which it
	 * is attached and its AbstractImage
	 *
	 * @param coordinate the model-coordinate of the Cell
	 * @param gameGrid   the GameGrid to which the Cell is attached
	 * @param img        the image of the Cell
	 */
	protected Cell(Vector2D coordinate, AbstractImage img, GameGrid gameGrid)
	{
		super(coordinate, gameGrid);
		this.img = img;
	}

	/**
	 * getDrawing() simply gets the drawing with image img
	 *
	 * @return the Drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		return getDrawing(img);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/