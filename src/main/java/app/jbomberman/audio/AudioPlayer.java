package app.jbomberman.audio;

import app.jbomberman.util.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AudioPlayer is the AudioPlayer needed to reproduce sounds.
 * It uses the singleton pattern as we want just one AudioPlayer
 * for the whole program.
 *
 * @author gp
 */
public class AudioPlayer
{
    /**
     * players is a mapping between the file being reproduced and the actual list
     * of threads playing it
     */
    private final Set<Clip> players;

    /**
     * audioPlayer is the singleton for AudioPlayer class
     */
    private static AudioPlayer audioPlayer;

    /**
     * pause is a boolean that represents whether the AudioPlayer has been paused
     */
    private boolean pause;

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

    private final Logger logger;

    /**
     * Construct the player. It's marked private as it must only be called by
     * the getInstance() function
     */
    private AudioPlayer()
    {
        players = Collections.synchronizedSet(new HashSet<>());
        pause = false;
        logger = LogManager.getLogger(getClass());
    }

    private Map<String, byte[]> cachedSounds = new HashMap<>();

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
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            cachedSounds.computeIfAbsent(path, p -> {
                try (InputStream in = Resources.getResourceAsStream(p)) {
                    return in.readAllBytes();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(cachedSounds.get(path)))));
            clip.start();
            players.add(clip);
        } catch (LineUnavailableException e) {
            System.err.println("error while playing " + path);
	    e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new AudioHandle(clip, path);
    }

    /**
     * stopAll() stops all the Clip's running
     */
    public void stopAll()
    {
        synchronized (players) {
            players.forEach(Line::close);
            players.clear();
        }
    }

    /**
     * pausePlaying() is used to set the pause flag and pause the playing of every player
     */
    public void pausePlaying()
    {
        setPause(true);
        players.forEach(DataLine::stop);
    }

    /**
     * resumePlaying() is used to tell the players to resume playing
     */
    public void resumePlaying()
    {
        setPause(false);
        players.forEach(DataLine::start);
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
}

