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

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Threads;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ThreadPlayer is the actual thread playing sounds. These are
 * spawned by {@link AudioPlayer} as independent threads. Outside this package, no other
 * class must directly call ThreadPlayer's methods. In fact, all the communication
 * between clients and ThreadPlayers must pass through the corresponding {@link AudioHandle}
 *
 * @author gp
 * @see AudioPlayer
 * @see ThreadPlayer
 */
class ThreadPlayer implements Runnable
{
	/**
	 * BUFFER_SIZE indicates the chunk size in bytes
	 * for efficient loading of audio files
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * After having stopped all the sounds, there is still a short period of time
	 * over which you can still here a bit of noise. So WAIT_TIME_MILLIS is the
	 * time in milliseconds for which any player will wait before playing a
	 * new sound
	 */
	private static final int WAIT_TIME_MILLS = 800;

	/**
	 * Store the filename in which there is the audio to play
	 */
	private final String soundFile;

	/**
	 * stopAllTime contains the timestamp of the last stopAll call that the
	 * {@link AudioPlayer} received
	 */
	private final long stopAllTime;

	/**
	 * soundBytes keeps the bytes of the sound to play
	 */
	private final byte[] soundBytes;

	/**
	 * interruptedFlag is a flag that must be set to interrupt the current
	 * thread player. From the player's thread, there must be periodical checks
	 * to see whether this flag is set
	 */
	private volatile boolean interruptedFlag = false;


	/**
	 * Create a new ThreadPlayer. Its visibility is default as we should only be able
	 * to initialize ThreadPlayer's from the {@link AudioPlayer} class
	 *
	 * @param soundFile   filename that contains the audio to play
	 * @param soundBytes  the audio file loaded in a byte array
	 * @param stopAllTime the time of the last stop of all sounds
	 */
	/*~*/ThreadPlayer(String soundFile, byte[] soundBytes, long stopAllTime)
	{
		this.soundFile = soundFile;
		this.stopAllTime = stopAllTime;
		this.soundBytes = soundBytes;
	}

	/**
	 * isDone keeps track of whether the ThreadPlayer has finished playing
	 */
	private boolean isDone;

	/**
	 * run() is meant to be called by start() method and contains the main routine
	 * to play an audio file
	 */
	@Override
	public void run()
	{
		try (InputStream inputStream =
					 new BufferedInputStream(new ByteArrayInputStream(soundBytes));
				 AudioInputStream audioInputStream =
					 AudioSystem.getAudioInputStream(inputStream))
		{
			AudioFormat audioFormat = audioInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

			try (SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info)) {
				byte[] bufferBytes = new byte[BUFFER_SIZE];
				int readBytes;

				sourceDataLine.open(audioFormat);
				sourceDataLine.start();

				while (System.currentTimeMillis() - stopAllTime <= WAIT_TIME_MILLS)
					Threads.sleep(WAIT_TIME_MILLS / 20);

				while ((readBytes = audioInputStream.read(bufferBytes)) != -1 && !interruptedFlag) {
					while (AudioPlayer.getInstance().getPause())
						Threads.sleep(30);
					sourceDataLine.write(bufferBytes, 0, readBytes);
				}
				sourceDataLine.drain();
			}
		} catch (IOException e) {
			throw new JBomberManException(
				"Unable to read file `" + soundFile + "'");
		} catch (UnsupportedAudioFileException e) {
			throw new JBomberManException(
				"Unsupported AudioFileException for file `" + soundFile + "'");
		} catch (LineUnavailableException e) {
			throw new JBomberManException(
				"Line is unavailable for `" + soundFile + "'");
		} catch (IllegalArgumentException e) {
			throw new JBomberManException(
				"Probably there was an error while decoding " + soundFile);
		}
		isDone = true;
		AudioPlayer.getInstance().hasFinished(this);
	}

	/**
	 * isDone() must be called to see if the threadPlayer has finished
	 *
	 * @return whether the thread player has finished playing the sound
	 */
	public boolean isDone()
	{
		return isDone;
	}

	/**
	 * getSoundFileName() returns the name of the file containing the sound that
	 * this thread is playing
	 *
	 * @return the name of the file containing the sound that this thread is playing
	 */
	public String getSoundFileName()
	{
		return soundFile;
	}

	/**
	 * setInterrupted must be called to set the interruptedFlag
	 */
	public void setInterrupted()
	{
		interruptedFlag = true;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
