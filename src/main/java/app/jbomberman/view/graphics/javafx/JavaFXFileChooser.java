package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Maybe;
import app.jbomberman.view.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * JavaFXFileChooser is the javafx implementation of the FileChooser
 *
 * @author gp
 */
public class JavaFXFileChooser extends FileChooser
{
	/**
	 * ]
	 * Stage is the stage to which the FileChooser must be attached
	 */
	private final Stage stage;

	/**
	 * JavaFXFileChooser needs to know the stage to which it must be attached
	 *
	 * @param stage the stage to which the FileChooser must be attached
	 */
	/*~*/JavaFXFileChooser(Stage stage)
	{
		this.stage = stage;
	}

	/**
	 * getFile() is the method that must be called to wait for the user selecting
	 * the file
	 *
	 * @return the file that the user inserted. It's a Maybe as the user might
	 * have pressed exit without selecting any file at all
	 */
	@Override
	public Maybe<File> getFile()
	{
		javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

		File f = fileChooser.showOpenDialog(stage);

		return f == null ? Maybe.nothing() : Maybe.just(f);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
