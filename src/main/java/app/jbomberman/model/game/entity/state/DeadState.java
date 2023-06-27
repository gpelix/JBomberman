package app.jbomberman.model.game.entity.state;

import app.jbomberman.model.game.entity.Explosion;

/**
 * DeathState is the state of Stateful objects when they are dying
 *
 * @author gp
 */
public abstract class DeadState extends AbstractState
{
	/**
	 * protected constructor meant to be called by subclasses, that initializes
	 * tje DeadState from a Stateful
	 *
	 * @param stateFul the Stateful object
	 */
	protected DeadState(StateFul stateFul)
	{
		super(stateFul, true);
	}

	/**
	 * notifyExplosion() simply returns false as dying things cannot block
	 * explosions
	 *
	 * @param explosion the Explosion object
	 * @return false
	 */
	@Override
	public final boolean notifyExplosion(Explosion explosion)
	{
		return false; // Simply ignore
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
