package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Callback;
import app.jbomberman.view.AbstractGraphicLoop;
import javafx.animation.AnimationTimer;

public class JavaFXGraphicLoop extends AbstractGraphicLoop
{
    private final AnimationTimer animationTimer;

    public JavaFXGraphicLoop(final Callback callback)
    {
        animationTimer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                callback.run();
            }
        };
    }


    @Override
    public void start()
    {
        animationTimer.start();
    }
}
