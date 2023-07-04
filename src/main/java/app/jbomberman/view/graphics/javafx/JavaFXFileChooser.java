package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Maybe;
import app.jbomberman.view.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class JavaFXFileChooser extends FileChooser
{
	private final Stage stage;

	/*~*/JavaFXFileChooser(Stage stage)
	{
		this.stage = stage;
	}

	@Override
	public Maybe<File> getFile()
	{
		javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

		File f = fileChooser.showOpenDialog(stage);

		return f == null ? Maybe.nothing() : Maybe.just(f);
	}
}
