
package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.entity.Explosion;

public abstract class GameCharacterState<T>
{
	private final T character;

	protected GameCharacterState(T character)
	{
		this.character = character;
	}

	protected T getCharacter()
	{
		return character;
	}

	public abstract boolean notifyExplosion(Explosion explosion);

	public abstract void updateInModel();

	public abstract Drawing getDrawing();

	public abstract boolean isDisappearing();
}
