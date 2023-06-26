package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.entity.Explosion;

public abstract class AbstractState
{
	private final StateFul character;

	protected AbstractState(StateFul character)
	{
		this.character = character;
	}

	protected StateFul getCharacter()
	{
		return character;
	}

	public abstract boolean notifyExplosion(Explosion explosion);

	public abstract void updateInModel();

	public abstract Drawing getDrawing();

	public abstract boolean isDisappearing();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
