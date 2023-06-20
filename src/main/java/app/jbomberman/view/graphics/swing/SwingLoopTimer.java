package app.jbomberman.view.graphics.swing;

import app.jbomberman.util.Callback;
import app.jbomberman.view.LoopTimer;

import javax.swing.Timer;

/**
 * SwingLoopTimer is the concrete implementation of LoopTimer for java swing
 *
 * @author gp
 * @implNote we use a javax.swing.Timer as an underlying timer
 */
public class SwingLoopTimer extends LoopTimer
{
	/**
	 * the underlying timer that is used to call repeatedly the function
	 */
	private final Timer timer;

	/**
	 * construct a timer from a callback
	 *
	 * @param callback the callback to be called repeatedly
	 */
	public SwingLoopTimer(Callback callback)
	{
		timer = new Timer(1000 / 60, e -> callback.run());
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
