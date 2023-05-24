package app.jbomberman.audio;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Resources;
import app.jbomberman.util.Threads;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ThreadPlayer is the actual thread playing sounds. These are
 * spawned by AudioPlayer as independent threads
 *
 * @author gp
 */
public class ThreadPlayer extends Thread
{
    /**
     * BUFFER_SIZE indicates the chunk size for efficient loading of audio files
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * After all the sounds have been stopped, there is still a short period of time
     * over which you can still here a bit of noise. So WAIT_TIME_MILLIS is the
     * time in milliseconds for which any player will wait before playing a
     * new sound
     */
    private static final int WAIT_TIME_MILLS = 800;

    /**
     * Store the filename in which there is the audio to play
     */
    private final String soundFile;

    private final long stopAllTime;

    private final AudioPlayer audioPlayer;


    /**
     * Create a new ThreadPlayer. It's private as we should only be able
     * to initialize ThreadPlayer's from the AudioPlayer class
     *
     * @param soundFile filename that contains the audio to play
     */
    ThreadPlayer(String soundFile, long stopAllTime, AudioPlayer audioPlayer)
    {
        this.soundFile = soundFile;
        this.stopAllTime = stopAllTime;
        this.audioPlayer = audioPlayer;
    }

    /**
     * isPlaying returns if the thread isAlive() that is if the thread is playing
     * a sound
     *
     * @return whether the thread is playing an audio
     */
    public boolean isPlaying()
    {
        return isAlive();
    }

    /**
     * run() is meant to be called by start() method and contains the main routine
     * to play an audio file
     */
    @Override
    public void run()
    {
        try (InputStream inputStream =
                 new BufferedInputStream(Resources.getResourceAsStream(soundFile));
             AudioInputStream audioInputStream =
                 AudioSystem.getAudioInputStream(inputStream))
        {
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            try (SourceDataLine sourceDataLine =
                     (SourceDataLine) AudioSystem.getLine(info))
            {
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();

                byte[] bufferBytes = new byte[BUFFER_SIZE];
                int readBytes = -1;
                while (System.currentTimeMillis() - stopAllTime <= WAIT_TIME_MILLS)
                    Threads.sleep(WAIT_TIME_MILLS / 20);

                while ((readBytes = audioInputStream.read(bufferBytes)) != -1) {
                    while (audioPlayer.getPause())
                        Threads.sleep(200);
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
    }

    public String getSoundFileName()
    {
        return soundFile;
    }
}
