package app.jbomberman.model.game.entity;

import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

/**
 * Block is a class that represents a Block in the GameGrid
 *
 * @author gp
 */
public class Block extends Cell
{
	/**
	 * img is a String that contains the path of the image Block
	 */
	private static final String img = "block.png";

	/**
	 * public constructor of block from a model-coordinate and a GameGrid
	 *
	 * @param coordinate the model-coordinate of the Cell
	 * @param gameGrid   the GameGrid to which the Cell is attached
	 */
	public Block(Vector2D coordinate, GameGrid gameGrid)
	{
		super(coordinate,
			FlightWeightImagesFactory.getInstance().getImage(img),
			gameGrid);
	}

	/**
	 * updateInModel() does nothing as the Blocks never change state
	 */
	@Override
	public void updateInModel()
	{
		// DO NOTHING
	}

	/**
	 * notifyExplosion() simply returns true as Blocks always block
	 * explosions
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return true as blocks always stop the explosions from propagating further
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return true; // Just handle it
	}

	/**
	 * allowsBombs() returns false as bombs must not be placed on top of
	 * Blocks
	 *
	 * @return false
	 */
	@Override
	public boolean allowsBombs()
	{
		return false;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
