package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Callback;
import app.jbomberman.view.LoopTimer;
import javafx.animation.AnimationTimer;

public class JavaFXLoopTimer extends LoopTimer
{
	private final AnimationTimer timer;

	public JavaFXLoopTimer(Callback callback)
	{
		timer = new AnimationTimer()
		{
			@Override
			public void handle(long ignored)
			{
				callback.run();
			}
		};
	}

	@Override
	public void start()
	{
		timer.start();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
