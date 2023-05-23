
package app.jbomberman.model.game.entity.gamecharacter;

import app.jbomberman.JBomberManException;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.model.game.entity.state.GameCharacterState;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.function.Supplier;

/**
 * GameCharacter is essentially an Entity with the ability
 * to move
 *
 * @author gp
 */
public abstract class GameCharacter extends Entity
{

	private GameCharacterCapabilities capabilities;

	private Maybe<GameCharacterState> state = Maybe.nothing(); // This must
	// be initialized rightly after having called the constructor

	protected GameCharacter(Vector2D coordinate,
													GameCharacterCapabilities capabilities,
													GameGrid gameGrid)
	{
		super(coordinate, gameGrid);
		this.capabilities = capabilities;
	}

	/**
	 * move() method is responsible for moving a character. It must
	 * determine where it would like to go to, and then it should ask
	 * permission to do so to the MoveManager
	 */
	public abstract void move();

	public GameCharacterCapabilities getCapabilities()
	{
		return capabilities;
	}

	public void setCapabilities(GameCharacterCapabilities capabilities)
	{
		this.capabilities = capabilities;
	}

	public void setState(GameCharacterState state)
	{
		this.state = Maybe.just(state);
	}

	Supplier<JBomberManException> stateIsNothingError =
		() -> new JBomberManException(
			"State should be set right after calling the constructor");

	@Override
	public void updateInModel()
	{
		state.unwrapOrThrow(stateIsNothingError).updateInModel();
	}


	@Override
	public Drawing getDrawing()
	{
		return state.unwrapOrThrow(stateIsNothingError).getDrawing();
	}

	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return state.unwrapOrThrow(stateIsNothingError).notifyExplosion(explosion);
	}

	public boolean isDisappearing()
	{
		return state.unwrapOrThrow(stateIsNothingError).isDisappearing();
	}

	public GameCharacterState getState()
	{
		return state.unwrap();
	}
}
