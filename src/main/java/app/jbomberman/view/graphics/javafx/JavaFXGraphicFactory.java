package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Callback;
import app.jbomberman.view.GraphicFactory;
import app.jbomberman.view.LoopTimer;

public class JavaFXGraphicFactory extends GraphicFactory
{
	@Override
	public LoopTimer getLoopTimer(Callback callback)
	{
		return new JavaFXLoopTimer(callback);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
