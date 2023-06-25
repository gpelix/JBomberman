package app.jbomberman.model.game.entity;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

public class Fire extends Entity
{
	public enum FireType
	{
		CENTER, HORIZONTAL, VERTICAL, NORTH, SOUTH, EAST, WEST
	}

	private final ImageManager imageManager;

	public Fire(Vector2D coordinate, GameGrid gameGrid, ImageManager imageManager)
	{
		super(coordinate, gameGrid);

		this.imageManager = imageManager;
	}

	@Override
	public Drawing getDrawing()
	{
		if (imageManager.hasChanged())
			hasChanged();
		return getDrawing(imageManager.getImage());
	}

	@Override
	public void updateInModel()
	{
		if (imageManager.hasDoneOneCycle()) {
			FreeCell freeCell = getUnderlyingFreeCell();
			freeCell.removeFire(this);
		}
	}

	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return false;
	}

	@Override
	public boolean allowsBombs()
	{
		return true;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
