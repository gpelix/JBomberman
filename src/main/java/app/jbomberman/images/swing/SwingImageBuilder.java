package app.jbomberman.images.swing;

import app.jbomberman.JBomberManException;
import app.jbomberman.images.AbstractImageBuilder;
import app.jbomberman.util.Resources;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * SwingImageBuilder is the concrete implementation of AbstractImageBuilder used
 * with Java Swing
 *
 * @author gp
 */
public class SwingImageBuilder extends AbstractImageBuilder
{
    /**
     * @param path the path to the image in the images/ folder
     * @return the SwingImage loaded
     */
    @Override
    public SwingImage getImage(String path)
    {
        try {
            return new SwingImage(ImageIO.read(Resources.getResourceAsStream(path)));
        } catch (IOException e) {
            throw new JBomberManException("Unable to load " + path);
        }
    }
}
