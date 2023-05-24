package app.jbomberman.controller;

import app.jbomberman.util.Callback;
import javafx.animation.AnimationTimer;

/**
 * EventLoopHandler is a concrete implementation of javafx AnimationTimer.
 * It also provides a way to limit the frame spikes and make the game smooth and
 * to call an action when handle() gets File.separatorrun
 *
 * @author gp
 */
public class EventLoopHandler extends AnimationTimer
{
    /**
     * fps is the desired fps rate for the application
     */
    private int fps;

    /**
     * timeOfLastFrame is the number of nanoseconds since 1/1/70. 0 is a
     * correct default value as seeing such a long delta T the callback will
     * be called and timeOfLastFrame will be updated
     */
    private long timeOfLastFrame = 0;

    /**
     * callback is a Callback that gets called by handle every time the timer
     * executes
     */
    private final Callback callback;

    private final Controller controller;


    /**
     * Construct a timer specifying the callback to call when timer expires
     *
     * @param callback the callback to call when the timer executes
     */
    public EventLoopHandler(Controller controller, Callback callback, int fps)
    {
        this.callback = callback;
        this.controller = controller;
        this.fps = fps;
    }

    /**
     * Not to be called explicitly. It gets executed when the timer expires.
     * In my implementation, I do a pre-check to see if enough time has passed
     * since the last time handle() was called. If the time is enough (at
     * most the fps may double), the callback is called. Otherwise, the frame
     * is dropped
     *
     * @param now The timestamp of the current frame given in nanoseconds. This
     *            value will be the same for all {@code AnimationTimers} called
     *            during one frame.
     */
    @Override
    public void handle(long now)
    {
        long deltaT = now - timeOfLastFrame;
        if (1. / deltaT * 1e9 >= fps * 2)
            // Drop frame
            return;
        // Update time
        timeOfLastFrame = now;
        try {
            callback.run();
        } catch (Exception e) {
            e.printStackTrace();
            controller.close();
        }
    }
}
