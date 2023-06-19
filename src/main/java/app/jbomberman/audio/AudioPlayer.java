package app.jbomberman.audio;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Maybe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * AudioPlayer is the AudioPlayer needed to reproduce sounds.
 * It uses the singleton pattern as we want just one AudioPlayer
 * for the whole program.
 *
 * @author gp
 * @implNote It extends Thread as it needs to be able to
 * run simultaneously with the main thread
 */
public class AudioPlayer extends Thread
{
    /**
     * players is a mapping between the file being reproduced and the actual list
     * of threads playing it
     */
    private final Set<ThreadPlayer> players;

    /**
     * audioPlayer is the singleton for AudioPlayer class
     */
    private static volatile AudioPlayer audioPlayer;

    /**
     * stopAllTime registers the time at which all the ThreadPlayers have been stopped
     * It's then used by the newly spawned ThreadPlayers to check weather or not WAIT_TIME_MILLIS milliseconds
     * have passed or not. It's marked volatile as it needs to be accessed by many threads. As Optional is
     * immutable is thread-safe, and no other thread-safety measure must be picked
     */
    private volatile long stopAllTime;

    /**
     * AudioPlayer's thread is only responsible for joining the spawned threads
     * that play the sounds. So it has a busy wait loop that stops for a certain
     * number of milliseconds and threadJoinloopIntervalMillis is that time
     * interval
     */
    private volatile long threadJoinloopIntervalMillis = 1000;

    /**
     * SUFFERENCE_LEVEL_IN_THREADS is the desired number of threads that the
     * joinloop must join at each iteration
     */
    private static final long SUFFERENCE_LEVEL_IN_THREADS = 2;

    /**
     * MAX_JOINLOOP_INTERVAL_IN_MILLIS is the maximum number of
     * milliseconds for which the join loop can sleep
     */
    private static final long MAX_JOINLOOP_INTERVAL_IN_MILLIS = 10_000;

    /**
     * pause is a boolean that represents whether the AudioPlayer has been paused
     */
    private volatile boolean pause;

    /**
     * getInstance() returns the singleton
     *
     * @return the singleton
     */
    public static AudioPlayer getInstance()
    {
        if (audioPlayer == null) {
            audioPlayer = new AudioPlayer();
            audioPlayer.start();
            audioPlayer.setName("Audio Player");
        }
        return audioPlayer;
    }

    private final Logger logger;

    /**
     * Construct the player. It's marked private as it must only be called by
     * the getInstance() function
     */
    private AudioPlayer()
    {
        players = Collections.synchronizedSet(new HashSet<>());
        stopAllTime = 0;
        pause = false;
        logger = LogManager.getLogger(getClass());
    }

    /**
     * run() must be called by start. All it does is periodically check
     * if some spawned ThreadPlayer has ended, and in that case, it removes
     * it from the players set.
     */
    @Override
    public void run()
    {
        try {
            while (true) {
                logger.info("Sleep time is " + threadJoinloopIntervalMillis);
                Thread.sleep(threadJoinloopIntervalMillis);
                joinThreads();
                if (isInterrupted())
                    throw new InterruptedException();
            }
        } catch (InterruptedException e) {
            // We simply quit after shutting up all the ThreadPlayers and
            // set a fake AudioPlayer instead of the current one
            stopAll();
            // A fake AudioPlayer
            audioPlayer = new AudioPlayer()
            {
                @Override
                public void run()
                {
                    throw new JBomberManException(
                        "This is only a placeholder and not meant to be run");
                }

                @Override
                public AudioHandle playSound(String ignored)
                {
                    return new AudioHandle(Maybe.nothing());
                }
            };
        }
    }

    /**
     * joinThreads is a private method used to join with the dead threads
     * It's synchronized as we might want to be able to all it from different
     * threads, hence it's a critical section (we could call join from the two
     * threads at the same time on the same dead thread and one of the two
     * will join with an error)
     */
    private synchronized void joinThreads() throws InterruptedException
    {
        int joined = 0;
        synchronized (players) {
            java.util.Iterator<ThreadPlayer> i = players.iterator();

            while (i.hasNext()) {
                ThreadPlayer nextThread = i.next();

                if (!nextThread.isAlive()) {
                    logger.info("Joining " + nextThread.getSoundFileName());
                    joined++;
                    nextThread.join();
                    i.remove();
                } else {
                    logger.info(nextThread.getSoundFileName() + " is still " +
                        "playing");
                }
            }
        }


        if (joined > SUFFERENCE_LEVEL_IN_THREADS) {
            long newThreadJoinloopInterval =
                threadJoinloopIntervalMillis / (int) Math.pow(2, (double) (joined / SUFFERENCE_LEVEL_IN_THREADS));
            threadJoinloopIntervalMillis = (newThreadJoinloopInterval != 0) ?
                newThreadJoinloopInterval : 1;
        } else {
            threadJoinloopIntervalMillis = Math.min(
                threadJoinloopIntervalMillis * 2,
                MAX_JOINLOOP_INTERVAL_IN_MILLIS);
        }
    }

    /**
     * playSound is used to play a new sound
     *
     * @param path Path to the file containing the audio
     * @return ThreadPlayer that is in charge of playing the sound. It's only meant
     * to be passed back to AudioPlayer's stop() method as argument
     */
    public AudioHandle playSound(String path)
    {
        logger.info("Playing " + path);
        ThreadPlayer t = new ThreadPlayer(path, stopAllTime, this);
        t.start();
        players.add(t);
        return new AudioHandle(Maybe.just(t));
    }

    /**
     * stop() stops a {@link ThreadPlayer}. This method is meant also to be called directly
     * by the client to stop a single ThreadPlayer whose instance was returned
     * by playSound
     *
     * @param player the {@link ThreadPlayer} to be stopped
     */
    /*~*/void stop(ThreadPlayer player)
    {
        if (Thread.currentThread() == player)
            throw new JBomberManException("this thread is a player thread and is trying to " +
                "stop itself");
        player.setInterrupted();
    }

    /**
     * stopAll() stops all the {@link ThreadPlayer}'s
     */
    public void stopAll()
    {
        stopAllTime = System.currentTimeMillis();
        synchronized (players) {
            players.forEach(this::stop);
            players.clear();
        }
    }

    /**
     * close() is used to kill the AudioPlayer's thread. It also does some
     * cleanup. You should call this from the client
     */
    public void close()
    {
        this.interrupt();
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
     * setPause() sets the pause flag
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
}

