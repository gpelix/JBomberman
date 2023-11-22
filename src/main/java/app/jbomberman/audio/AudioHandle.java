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
 
package app.jbomberman.audio;

/**
 * AudioHandle is a wrapper around threadPlayer that provides an additional
 * layer to call ThreadPlayer's functions. We don't want general functions to
 * interact with the threadPlayer.
 * <p>
 * We really need this class because when the
 * AudioPlayer gets shut down, it won't play anything anymore, and it will not
 * create ThreadPlayers. So the clients would have to interact with objects of
 * type Maybe ThreadPlayers, they would have to know what went wrong, and it
 * would induce tight coupling between the behavior of the ThreadPlayer and
 * other packages
 *
 * @author gp
 * @see AudioPlayer
 * @see ThreadPlayer
 */
public class AudioHandle
{
	/**
	 * threadPlayer is the threadPlayer that this class wraps
	 * <p>
	 * its type is Maybe because if the AudioPlayer gets shut down,
	 * no more ThreadPlayers will be created and this will be Nothing
	 */
	private final ThreadPlayer threadPlayer;

	/**
	 * Constructor of AudioHandle from a ThreadPlayer
	 * <p>
	 * its visibility is default as we only want this package (and
	 * more specifically the AudioPlayer) to create AudioHandles
	 *
	 * @param threadPlayer the optional threadPlayer that this class will wrap
	 */
	/*~*/AudioHandle(ThreadPlayer threadPlayer)
	{
		this.threadPlayer = threadPlayer;
	}

	/**
	 * isPlaying() checks whether the threadPlayer is playing something
	 *
	 * @return whether the threadPlayer is playing something
	 */
	public boolean isPlaying()
	{
		return !threadPlayer.isDone();
	}

	/**
	 * getSoundFileName() gets the name of the file currently playing
	 *
	 * @return the name of the file currently playing
	 */
	public String getSoundFileName()
	{
		return threadPlayer.getSoundFileName();
	}

	/**
	 * stop() stops the playing of the sound
	 */
	public void stop()
	{
		threadPlayer.setInterrupted();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
