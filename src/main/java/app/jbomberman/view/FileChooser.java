package app.jbomberman.view;

import app.jbomberman.util.Maybe;

import java.io.File;

public abstract class FileChooser
{
	/**
	 * getFile() must be used to get the file chosen by the user
	 *
	 * @return the File chosen by the user
	 */
	public abstract Maybe<File> getFile();
}
