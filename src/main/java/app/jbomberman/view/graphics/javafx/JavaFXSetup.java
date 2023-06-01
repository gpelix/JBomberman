package app.jbomberman.view.graphics.javafx;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Maybe;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFXSetup is a simple class whose only aim is to create a javafx thread
 * and call the callback provided into that thread
 *
 * @author gp
 */
public class JavaFXSetup extends Application
{
    /**
     * callback is the callback to be called in the javafx thread
     */
    private static Maybe<Callback> callback = Maybe.nothing();

    /**
     * start() is the javafx thread entry point. This is NOT meant to be called
     * by any function at all in my program
     *
     * @param ignored the parameter is ignored
     */
    @Override
    public void start(Stage ignored)
    {
        callback.doOrThrow(Callback::run, () -> new JBomberManException(
            "Callback is uninitialized"
        ));
    }

    /**
     * initialize() must be called by the View to initialize javafx
     *
     * @param callback the callback to be called inside the javafx thread
     */
    public static void initialize(Callback callback)
    {
        JavaFXSetup.callback = Maybe.just(callback);
        launch();
    }
}
