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
 
package app.jbomberman.util;

/**
 * Timer is a class that models the behaviour of a timer
 *
 * @author gp
 */
public class Timer
{
	/**
	 * a maybe that represents the time of the start of the timer. It's a Maybe
	 * because if it hasn't started yet we have no startTime at all
	 */
	private Maybe<Long> startTime;

	/**
	 * duration is the amount of time for which the timer has to run
	 * in milliseconds
	 */
	private long duration;

	/**
	 * public constructor specifying the duration of the timer interval
	 *
	 * @param durationInMillis the duration of the timer interval
	 */
	public Timer(long durationInMillis)
	{
		reset();
		this.duration = durationInMillis;
	}

	/**
	 * start is used to start the timer
	 */
	public void start()
	{
		startTime = Maybe.just(System.currentTimeMillis());
	}

	/**
	 * timeIsUp() checks whether the timer is "ringing"
	 *
	 * @return true if time is up
	 * @throws RuntimeException if the timer hasn't started yet
	 */
	public boolean timeIsUp()
	{
		return elapsed() >= duration;
	}

	/**
	 * elapsed() must be called sfter start and it returns the milliseconds that
	 * have passed since the timer's start
	 *
	 * @return the milliseconds for which the timer has been running
	 * @throws RuntimeException if the timer hasn't started yet
	 */
	private long elapsed()
	{
		return startTime.computeOrThrow(
			start -> System.currentTimeMillis() - start,
			() -> new RuntimeException("Timer not started")
		);
	}

	/**
	 * hasStarted() checks whether the timer has started or not
	 *
	 * @return true if the timer has started
	 */
	public boolean hasStarted()
	{
		return startTime.isJust();
	}

	/**
	 * reset() must be used to reset the timer but its duration.
	 * Note that, after reset, start() must be called to start it again
	 */
	public void reset()
	{
		startTime = Maybe.nothing();
	}

	/**
	 * @return the number of milliseconds to the end of timer
	 */
	public long millisToEnd()
	{
		return duration - elapsed();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
