package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.entity.Explosion;

/**
 * AbstractState is the common superclass for all the states
 *
 * @author gp
 */
public abstract class AbstractState
{
	/**
	 * stateFul is the StateFul that has this as State
	 */
	private final StateFul stateFul;

	/**
	 * isDisappearing is a Flag that is true if and only if the Stateful is
	 * disappearing
	 */
	private final boolean isDisappearing;

	/**
	 * protected constructor of AbstractState from a StateFul object
	 *
	 * @param stateFul       the Stateful to set
	 * @param isDisappearing whether the Stateful is disappearing
	 */
	protected AbstractState(StateFul stateFul, boolean isDisappearing)
	{
		this.stateFul = stateFul;
		this.isDisappearing = isDisappearing;
	}

	/**
	 * getStateful() gets the stateful object whose state is this
	 *
	 * @return the Stateful object
	 */
	protected StateFul getStateful()
	{
		return stateFul;
	}

	/**
	 * notifyExplosion() must be overridden in subclasses and must contain the
	 * logic to handle an explosion by the Stateful
	 *
	 * @param explosion the Explosion object
	 * @return whether the explosion was stopped by the Statful
	 */
	public abstract boolean notifyExplosion(Explosion explosion);

	/**
	 * updateInModel() must be overridden in subclasses and must be used to
	 * update the StateFul object
	 */
	public abstract void updateInModel();

	/**
	 * getDrawing() must be overridden in subclasses and must be used to
	 * get the drawing associated with the Stateful
	 *
	 * @return the drawing associated with the Stateful
	 */
	public abstract Drawing getDrawing();

	/**
	 * isDisappearing() must be used to check whether the StatFul is disappearing or not
	 *
	 * @return whether the StatFul is disappearing
	 */
	public final boolean isDisappearing()
	{
		return isDisappearing;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
