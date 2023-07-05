package app.jbomberman.view.graphics.swing;

import app.jbomberman.util.Maybe;
import app.jbomberman.view.FileChooser;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * SwingFileChooser is the java swing implementation of the FileChooser for java
 * swing
 *
 * @author gp
 */
public class SwingFileChooser extends FileChooser
{
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
		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int r = j.showOpenDialog(null);

		return (r == JFileChooser.APPROVE_OPTION) ?
			Maybe.just(j.getSelectedFile()) :
			Maybe.nothing();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/

