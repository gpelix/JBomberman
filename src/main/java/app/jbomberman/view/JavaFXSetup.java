
package app.jbomberman.view;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Maybe;
import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFXSetup extends Application
{
	private static Maybe<Callback> callback = Maybe.nothing();

	@Override
	public void start(Stage primaryStage)
	{
		callback.doOrThrow(Callback::run, () -> new JBomberManException(
			"Callback is uninitialized"
		));
	}

	public static void initialize(Callback callback)
	{
		JavaFXSetup.callback = Maybe.just(callback);
		launch();
	}
}
