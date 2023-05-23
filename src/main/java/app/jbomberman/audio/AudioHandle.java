
package app.jbomberman.audio;

import app.jbomberman.util.Maybe;

public class AudioHandle
{
    private Maybe<ThreadPlayer> threadPlayer;

    AudioHandle(Maybe<ThreadPlayer> threadPlayer)
    {
        this.threadPlayer = threadPlayer;
    }

    public boolean isPlaying()
    {
        return threadPlayer.compute(true, ThreadPlayer::isPlaying);
    }

    public Maybe<String> getSoundFileName()
    {
        return threadPlayer.maybeCompute(ThreadPlayer::getSoundFileName);
    }

    public void stop()
    {
        threadPlayer.maybeDo(tPlayer -> AudioPlayer.getInstance().stop(tPlayer));
    }
}
