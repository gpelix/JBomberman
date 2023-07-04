package app.jbomberman.view.graphics.swing;

import app.jbomberman.util.Maybe;
import app.jbomberman.view.FileChooser;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class SwingFileChooser extends FileChooser
{
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
