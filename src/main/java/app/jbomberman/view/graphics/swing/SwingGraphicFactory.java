package app.jbomberman.view.graphics.swing;

import app.jbomberman.util.Callback;
import app.jbomberman.view.GraphicFactory;
import app.jbomberman.view.LoopTimer;

public class SwingGraphicFactory extends GraphicFactory
{
	@Override
	public LoopTimer getLoopTimer(Callback callback)
	{
		return new SwingLoopTimer(callback);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
