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
