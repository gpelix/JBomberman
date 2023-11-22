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
 
package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Callback;
import app.jbomberman.view.LoopTimer;
import javafx.animation.AnimationTimer;

/**
 * JavaFXLoopTimer is the concrete implementation of LoopTimer for javafx
 * <p>
 * Under the hood we use an AnimationTimer
 *
 * @author gp
 */
public class JavaFXLoopTimer extends LoopTimer
{
	/**
	 * timer is the underlying AnimationTimer that we use to call repeatedly the
	 * callback
	 */
	private final AnimationTimer timer;

	/**
	 * construct a LoopTimer from a callback
	 *
	 * @param callback the callback to call repeatedly
	 */
	public JavaFXLoopTimer(Callback callback)
	{
		timer = new AnimationTimer()
		{
			/**
			 * handle will call the callback
			 *
			 * @param ignored
			 *            The timestamp of the current frame given in nanoseconds. This
			 *            value will be the same for all {@code AnimationTimers} called
			 *            during one frame.
			 */
			@Override
			public void handle(long ignored)
			{
				callback.run();
			}
		};
	}

	/**
	 * start() is called to start the LoopTimer
	 */
	@Override
	public void start()
	{
		timer.start();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
