package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Callback;
import app.jbomberman.view.AbstractGraphicFactory;
import app.jbomberman.view.AbstractGraphicLoop;

public class JavaFXGraphicFactory extends AbstractGraphicFactory
{
    @Override
    public AbstractGraphicLoop buildLoopTimer(Callback callback)
    {
        return new JavaFXGraphicLoop(callback);
    }
}
