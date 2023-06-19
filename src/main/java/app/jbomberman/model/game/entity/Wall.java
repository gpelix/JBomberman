package app.jbomberman.model.game.entity;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.entity.state.GameCharacterState;
import app.jbomberman.model.game.entity.state.StateFul;
import app.jbomberman.model.game.entity.state.WallAliveState;
import app.jbomberman.model.game.entity.state.WallDeadState;
import app.jbomberman.util.Vector2D;

public class Wall extends Entity implements StateFul
{
	private GameCharacterState state;

	public Wall(Vector2D coordinate, GameGrid gameGrid,
				ImageManager aliveImages, ImageManager deathImages)
	{
		super(coordinate, gameGrid);

		state = new WallAliveState(this, new WallDeadState(this,
			deathImages), aliveImages);
	}

	@Override
	public void updateInModel()
	{
		state.updateInModel();
	}

	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return state.notifyExplosion(explosion);
	}

	@Override
	public boolean allowsBombs()
	{
		return !(state instanceof WallAliveState);
	}

	@Override
	public Drawing getDrawing()
	{
		return state.getDrawing();
	}

	public void setState(GameCharacterState state)
	{
		this.state = state;
	}

	public boolean isDisappearing()
	{
		return state.isDisappearing();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
