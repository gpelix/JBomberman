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
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    private Map<String, Clip> cachedSounds = new HashMap<>();

    /**
     * playSound is used to play a new sound
     *
     * @param path Path to the file containing the audio
     * @return ThreadPlayer that is in charge of playing the sound. It's only meant
     * to be passed back to AudioPlayer's stop() method as argument
     */
    public AudioHandle playSound(String path)
    {
        joinEndedSounds();
        logger.info("Playing " + path);
        cachedSounds.computeIfAbsent(path, p -> {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(Resources.getResourceAsStream(path))));
                return clip;
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Clip clip = cachedSounds.get(path);
        clip.setFramePosition(0);
        clip.start();
        players.add(clip);

        return new AudioHandle(clip, path);
    }

    private void joinEndedSounds()
    {
        List<Clip> clips = players.stream().filter(DataLine::isRunning).toList();
        clips.forEach(players::remove);
    }

    /**
     * stopAll() stops all the Clip's running
     */
    public void stopAll()
    {
        players.forEach(Line::close);
        players.clear();
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

