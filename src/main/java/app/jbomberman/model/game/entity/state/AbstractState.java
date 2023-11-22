/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
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
