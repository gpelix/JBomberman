package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.BomberMan;
import app.jbomberman.model.game.entity.Explosion;

public class BomberManDeadState extends DeadState
{
	private final BomberMan bomberMan;

	private final ImageManager deathImageManager;

	public BomberManDeadState(BomberMan bomberMan,
														ImageManager deathImageManager)
	{
		super(bomberMan);
		this.bomberMan = bomberMan;
		this.deathImageManager = deathImageManager;
	}

	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return false; // Just ignore
	}

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
