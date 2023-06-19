package app.jbomberman.view.graphics.swing;

import app.jbomberman.util.Callback;
import app.jbomberman.view.LoopTimer;

import javax.swing.Timer;

public class SwingLoopTimer extends LoopTimer
{
	private final Timer timer;

	public SwingLoopTimer(Callback callback)
	{
		timer = new Timer(1000 / 60, e -> callback.run());
	}

	@Override
	public void start()
	{
		timer.start();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
