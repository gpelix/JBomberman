package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.BomberMan;

/**
 * BomberManDeadState is the state of BomberMan when he's dead
 *
 * @author gp
 */
public class BomberManDeadState extends DeadState
{
	/**
	 * bomberMan is a reference to the bomberMan that has this as state
	 */
	private final BomberMan bomberMan;

	/**
	 * deathImageManager is bomberman's ImageManager
	 */
	private final ImageManager deathImageManager;

	/**
	 * public constructor of BomberManDeadState
	 *
	 * @param bomberMan         the bomberman whose state is this one
	 * @param deathImageManager bomberman's ImageManager
	 */
	public BomberManDeadState(BomberMan bomberMan,
														ImageManager deathImageManager)
	{
		super(bomberMan);
		this.bomberMan = bomberMan;
		this.deathImageManager = deathImageManager;
	}

	/**
	 * updateInModel() waits until the death sound has ended and then ends the game
	 */
	@Override
	public void updateInModel()
	{
		if (bomberMan.getSoundHandle().fromJust().getSoundFileName().equals(BomberMan.getBombermanDeathSound()) &&
			deathImageManager.hasDoneOneCycle())
		{
			GameGrid gameGrid = bomberMan.getGameGrid();
			gameGrid.get(
				gameGrid.getCenterInGridCoord(bomberMan)
			).removeCharacter(bomberMan);
			Model.getInstance().getGame().endGame(false);
		}
	}

	/**
	 * getDrawing() gets the BomberMan's Drawing
	 *
	 * @return the Drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		AbstractImage image = deathImageManager.getImage();
		if (deathImageManager.hasChanged())
			bomberMan.hasChanged();
		return bomberMan.getDrawing(image);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
