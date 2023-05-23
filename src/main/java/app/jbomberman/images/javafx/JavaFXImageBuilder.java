
package app.jbomberman.images.javafx;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.AbstractImageBuilder;
import app.jbomberman.util.Resources;
import javafx.scene.image.Image;

public class JavaFXImageBuilder extends AbstractImageBuilder
{
	@Override
	public AbstractImage getImage(String path)
	{
		return new JavaFXImage(new Image(Resources.getResourceAsStream(path)));
	}
}
