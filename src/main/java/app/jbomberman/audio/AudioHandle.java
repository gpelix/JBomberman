package app.jbomberman.audio;

import app.jbomberman.util.Maybe;

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
 * @see ThreadPlayer
 */
public class AudioHandle
{
    /**
     * threadPlayer is the threadPlayer that this class wraps
     *
     * @implNote its type is Maybe because if the AudioPlayer gets shut down,
     * no more ThreadPlayers will be created and this will be Nothing
     */
    private final Maybe<ThreadPlayer> threadPlayer;

    /**
     * Constructor of AudioHandle from a ThreadPlayer
     *
     * @param threadPlayer the optional threadPlayer that this class will wrap
     * @implNote its visibility is default as we only want this package (and
     * more specifically the AudioPlayer) to create AudioHandles
     */
    AudioHandle(Maybe<ThreadPlayer> threadPlayer)
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
        return threadPlayer.compute(true, ThreadPlayer::isPlaying);
    }

    /**
     * getSoundFileName() gets the name of the file currently playing
     *
     * @return the name of the file currently playing
     */
    public Maybe<String> getSoundFileName()
    {
        return threadPlayer.maybeCompute(ThreadPlayer::getSoundFileName);
    }

    /**
     * stop() stops the playing of the sound
     */
    public void stop()
    {
        threadPlayer.maybeDo(AudioPlayer.getInstance()::stop);
    }
}
