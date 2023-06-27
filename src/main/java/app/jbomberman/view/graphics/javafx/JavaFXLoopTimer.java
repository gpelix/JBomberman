package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Callback;
import app.jbomberman.view.LoopTimer;
import javafx.animation.AnimationTimer;

/**
 * JavaFXLoopTimer is the concrete implementation of LoopTimer for javafx
 * <p>
 * Under the hood we use an AnimationTimer
 *
 * @author gp
 */
public class JavaFXLoopTimer extends LoopTimer
{
	/**
	 * timer is the underlying AnimationTimer that we use to call repeatedly the
	 * callback
	 */
	private final AnimationTimer timer;

	/**
	 * construct a LoopTimer from a callback
	 *
	 * @param callback the callback to call repeatedly
	 */
	public JavaFXLoopTimer(Callback callback)
	{
		timer = new AnimationTimer()
		{
			/**
			 * handle will call the callback
			 *
			 * @param ignored
			 *            The timestamp of the current frame given in nanoseconds. This
			 *            value will be the same for all {@code AnimationTimers} called
			 *            during one frame.
			 */
			@Override
			public void handle(long ignored)
			{
				callback.run();
			}
		};
	}

	/**
	 * start() is called to start the LoopTimer
	 */
	@Override
	public void start()
	{
		timer.start();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
