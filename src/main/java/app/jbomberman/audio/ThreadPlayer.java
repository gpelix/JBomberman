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
 * spawned by {@link AudioPlayer} as independent threads. Outside this package, no other
 * class must directly call ThreadPlayer's methods. In fact, all the communication
 * between clients and ThreadPlayers must pass through the corresponding {@link AudioHandle}
 *
 * @author gp
 * @see AudioPlayer
 * @see ThreadPlayer
 */
class ThreadPlayer extends Thread
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

    private final AudioPlayer audioPlayer;


    /**
     * Create a new ThreadPlayer. Its visibility is default as we should only be able
     * to initialize ThreadPlayer's from the {@link AudioPlayer} class
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
     * isPlaying returns if the thread isAlive(); that is if the thread is playing
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

            try (SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info)) {
                byte[] bufferBytes = new byte[BUFFER_SIZE];
                int readBytes;

                sourceDataLine.open(audioFormat);
                sourceDataLine.start();

                while (System.currentTimeMillis() - stopAllTime <= WAIT_TIME_MILLS)
                    Threads.sleep(WAIT_TIME_MILLS / 20);

                while ((readBytes = audioInputStream.read(bufferBytes)) != -1 && !interruptedFlag) {
                    while (audioPlayer.getPause())
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

    private boolean interruptedFlag = false;

    public void setInterrupted()
    {
        interruptedFlag = true;
    }
}
