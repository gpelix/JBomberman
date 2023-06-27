package app.jbomberman.model.game.entity.state;

/**
 * AliveState is the state in which entities are alive
 *
 * @author gp
 */
public abstract class AliveState extends AbstractState
{
	/**
	 * deathState is the state to set when the Stateful dies
	 */
	private final AbstractState deathState;

	/**
	 * protected constructor meant to be called by subclasses to initialize the
	 * AliveState from a Stateful called entity and a deathState that is the
	 * state to set when the StateFul dies
	 *
	 * @param entity     the Stateful whose state is this one
	 * @param deathState the state to set when the StateFul dies
	 */
	protected AliveState(StateFul entity, AbstractState deathState)
	{
		super(entity, false);

		this.deathState = deathState;
	}

	/**
	 * die() is a protected method meant to be called by subclasses to transition
	 * to the next state (deathState)
	 */
	protected void die()
	{
		getStateful().setState(deathState);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
