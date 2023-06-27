package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.entity.state.AbstractState;
import app.jbomberman.model.game.entity.state.StateFul;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.function.Supplier;

/**
 * GameCharacter is an Entity with the ability to move.
 * We use the state pattern to provide added flexibility to the GameCharacters
 *
 * @author gp
 */
public abstract class GameCharacter extends Entity implements StateFul
{
	/**
	 * capabilities are the GameCharacterCapabilities of the GameCharacter
	 */
	private GameCharacterCapabilities capabilities;

	/**
	 * state must be initialized rightly after having called the constructor
	 * and represents the state of the GameCharacter.
	 * We don't set state right in the constructor to provide more flexibility
	 */
	private Maybe<AbstractState> state = Maybe.nothing();

	/**
	 * public constructor of GameCharacter
	 *
	 * @param capabilities the capabilities of the GameCharacter
	 * @param coordinate   the model-coordinate of the GameCharacter
	 * @param gameGrid     the GameGrid to which the GameCharacter is attached
	 */
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

	/**
	 * getCapabilities() gets the capabilities of the GameCharacter
	 *
	 * @return the capabilities the GameCharacter has
	 */
	public GameCharacterCapabilities getCapabilities()
	{
		return capabilities;
	}

	/**
	 * setCapabilities() sets the capabilities of the GameCharacter
	 *
	 * @param capabilities the new capabilities for the GameCharacter
	 */
	public void setCapabilities(GameCharacterCapabilities capabilities)
	{
		this.capabilities = capabilities;
	}

	/**
	 * setState() sets the state of the Character
	 *
	 * @param state the new state to be set
	 */
	public void setState(AbstractState state)
	{
		this.state = Maybe.just(state);
	}

	/**
	 * stateIsNothingError is a Supplier of exceptions that must be thrown if the
	 * state variable is Nothing
	 */
	private final Supplier<JBomberManException> stateIsNothingError =
		() -> new JBomberManException(
			"State should be set right after calling the constructor");

	/**
	 * updateInModel() calls the updateInModel() method on the state
	 */
	@Override
	public void updateInModel()
	{
		state.unwrapOrThrow(stateIsNothingError).updateInModel();
	}

	/**
	 * getDrawing() calls the getDrawing() method on the state
	 */
	@Override
	public Drawing getDrawing()
	{
		return state.unwrapOrThrow(stateIsNothingError).getDrawing();
	}

	/**
	 * notifyExplosion() calls the notifyExplosion() method on the state
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return state.unwrapOrThrow(stateIsNothingError).notifyExplosion(explosion);
	}

	/**
	 * isDisappearing() calls the isDisappearing() method on the state
	 *
	 * @return whether the GameCharacter is disappearing
	 */
	public boolean isDisappearing()
	{
		return state.unwrapOrThrow(stateIsNothingError).isDisappearing();
	}

	/**
	 * getState() gets the state of the GameCharacter
	 *
	 * @return the state of the GameCharacter
	 */
	public AbstractState getState()
	{
		return state.unwrapOrThrow(stateIsNothingError);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
