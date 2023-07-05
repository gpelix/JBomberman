package app.jbomberman.view;

import app.jbomberman.util.Maybe;

import java.io.File;

/**
 * FileChooser is a class that must be used to create a dialog that will let the
 * user pick a file from many
 *
 * @author gp
 */
public abstract class FileChooser
{
	/**
	 * getFile() must be used to get the file chosen by the user
	 *
	 * @return the File chosen by the user
	 */
	public abstract Maybe<File> getFile();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
