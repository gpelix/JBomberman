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
 * CallbackOnce is a special type of Callback that has the runOnce method that
 * even though it gets called many times, it will only execute once.
 * <p>
 * Even though it's a SAM, it cannot be made interface as we need the
 * hasRun field. This is a bit nasty as we cannot declare CallbackOnce's objects
 * on the fly with a method reference or a lambda
 *
 * @author gp
 */
public abstract class CallbackOnce implements Callback
{
	/**
	 * hasRun is a boolean that is true if and only if the runOnce method
	 * has been run once
	 */
	private boolean hasRun = false;

	/**
	 * runOnce calls run if and only if it has not called before
	 */
	public void runOnce()
	{
		if (!hasRun) {
			run();
			hasRun = true;
		}
	}

	/**
	 * reset() resets the CallbackOnce and a runOnce call would after this be
	 * re-executed
	 */
	public void reset()
	{
		hasRun = false;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
