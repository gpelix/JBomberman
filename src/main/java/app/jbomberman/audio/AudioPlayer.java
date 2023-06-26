package app.jbomberman.audio;

import app.jbomberman.util.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AudioPlayer is the AudioPlayer needed to reproduce sounds.
 * It uses the singleton pattern as we want just one AudioPlayer
 * for the whole program.
 *
 * @author gp
 * @implNote It extends Thread as it needs to be able to
 * run simultaneously with the main thread
 * @see ThreadPlayer
 */
public class AudioPlayer
{
	/**
	 * audioPlayer is the singleton for AudioPlayer class
	 */
	private static AudioPlayer audioPlayer;

	/**
	 * stopAllTime registers the time at which all the ThreadPlayers have been stopped
	 * It's then used by the newly spawned ThreadPlayers to check weather or not WAIT_TIME_MILLIS milliseconds
	 * have passed or not. It's marked volatile as it needs to be accessed by many threads. As Optional is
	 * immutable is thread-safe, and no other thread-safety measure must be picked
	 */
	private long stopAllTime;

	/**
	 * executorsPool is the pool of Threads from which will be taken the ones
	 * to run ThreadPlayers
	 */
	private final ExecutorService executorsPool;

	/**
	 * audioHandles is the list of audio-handles created.
	 */
	private final Set<ThreadPlayer> audioHandles = new HashSet<>();

	/**
	 * pause is a boolean that represents whether the AudioPlayer has been paused.
	 * It's meant to be kept in order to know the state of the audioPlayer and
	 * {@link ThreadPlayer}'s must periodically check whether this flag is set
	 */
	private volatile boolean pause;

	/**
	 * soundPoll is a map Path->sound read in byte[], and it's used to avoid opening
	 * and reading many files many times. It's a way of caching sounds
	 */
	private final Map<String, byte[]> soundPool;

	/**
	 * getInstance() returns the singleton
	 *
	 * @return the singleton
	 */
	public static AudioPlayer getInstance()
	{
		if (audioPlayer == null)
			audioPlayer = new AudioPlayer();
		return audioPlayer;
	}

	/**
	 * logger
	 */
	private final Logger logger;

	/**
	 * Construct the player. It's marked private as it must only be called by
	 * the getInstance() function
	 */
	private AudioPlayer()
	{
		stopAllTime = 0;
		executorsPool = Executors.newCachedThreadPool();
		pause = false;
		logger = LogManager.getLogger(getClass());
		soundPool = new HashMap<>();
	}

	/**
	 * playSound is used to play a new sound
	 *
	 * @param path Path to the file containing the audio
	 * @return the audio handle that is in charge of playing the sound.
	 * @see AudioHandle
	 */
	public AudioHandle playSound(String path)
	{
		logger.info("Playing " + path);
		if (!soundPool.containsKey(path)) {
			try (InputStream in = new BufferedInputStream(Resources.getResourceAsStream(path))) {
				soundPool.put(path, in.readAllBytes());
			} catch (IOException e) {
				logger.fatal("Unable to read file " + path + ". Caught exception " + e.getMessage());
			}
		}
		ThreadPlayer threadPlayer = new ThreadPlayer(path, soundPool.get(path), stopAllTime);
		executorsPool.execute(threadPlayer);
		audioHandles.add(threadPlayer);

		return new AudioHandle(threadPlayer);
	}

	/**
	 * stopAll() stops all the {@link ThreadPlayer}'s
	 */
	public void stopAll()
	{
		stopAllTime = System.currentTimeMillis();
		audioHandles.forEach(ThreadPlayer::setInterrupted);
	}

	/**
	 * close() is used to kill the AudioPlayer's thread. It also does some
	 * cleanup. You should call this from the client
	 */
	public void close()
	{
		stopAll();
		executorsPool.shutdown();
		executorsPool.shutdownNow();
	}

	/**
	 * pausePlaying() is used to set the pause flag and pause the playing of every player
	 */
	public void pausePlaying()
	{
		setPause(true);
	}

	/**
	 * resumePlaying() is used to tell the players to resume playing
	 */
	public void resumePlaying()
	{
		setPause(false);
	}

	/**
	 * setPause() sets the pause flag. It's
	 *
	 * @param pause the flag to assign to pause
	 */
	private void setPause(boolean pause)
	{
		this.pause = pause;
	}

	/**
	 * getPause flag is used by {@link ThreadPlayer}s to determine whether they
	 * have to play or not a sound
	 *
	 * @return the value of pause flag
	 */
	/*~*/boolean getPause()
	{
		return pause;
	}

	/**
	 * Whenever an ThreadPlayer terminates it needs to call this function
	 *
	 * @param threadPlayer the thread player that has finished playing
	 */
	public void hasFinished(ThreadPlayer threadPlayer)
	{
		audioHandles.remove(threadPlayer);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/