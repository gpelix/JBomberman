package app.jbomberman.audio;

import javax.sound.sampled.Clip;

/**
 * AudioHandle is a wrapper around threadPlayer that provides an additional
 * layer to call ThreadPlayer's functions. We don't want general functions to
 * interact with the threadPlayer.
 *
 * @author gp
 * @implNote We really need this class because when the
 * AudioPlayer gets shut down, it won't play anything anymore, and it will not
 * create ThreadPlayers. So the clients would have to interact with objects of
 * type Maybe< ThreadPlayers>, they would have to know what went wrong, and it
 * would induce tight coupling between the behavior of the ThreadPlayer and
 * other packages
 * @see AudioPlayer
 */
public class AudioHandle
{
    /**
     * threadPlayer is the threadPlayer that this class wraps
     *
     * @implNote its type is Maybe because if the AudioPlayer gets shut down,
     * no more ThreadPlayers will be created and this will be Nothing
     */
    private final Clip threadPlayer;

    private final String soundFileName;

    /**
     * Constructor of AudioHandle from a Clip
     *
     * @param clip the clip that this class will wrap
     * @implNote its visibility is default as we only want this package (and
     * more specifically the AudioPlayer) to create AudioHandles
     */
    AudioHandle(Clip clip, String soundFileName)
    {
        this.threadPlayer = clip;
        this.soundFileName = soundFileName;
    }

    /**
     * isPlaying() checks whether the threadPlayer is playing something
     *
     * @return whether the threadPlayer is playing something
     */
    public boolean isPlaying()
    {
        return threadPlayer.isActive();
    }

    /**
     * getSoundFileName() gets the name of the file currently playing
     *
     * @return the name of the file currently playing
     */
    public String getSoundFileName()
    {
        return soundFileName;
    }

    /**
     * stop() stops the playing of the sound
     */
    public void stop()
    {
        threadPlayer.close();
    }
}
