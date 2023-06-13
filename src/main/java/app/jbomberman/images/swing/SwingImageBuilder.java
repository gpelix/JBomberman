package app.jbomberman.images.swing;

import app.jbomberman.JBomberManException;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.AbstractImageBuilder;
import app.jbomberman.util.Resources;

import javax.imageio.ImageIO;
import java.io.IOException;

public class SwingImageBuilder extends AbstractImageBuilder
{
    @Override
    public AbstractImage getImage(String path)
    {
        // TODO: I think the error is here! Should test it with the small program
        try {
            return new SwingImage(ImageIO.read(Resources.getResourceAsStream(path)));
        } catch (IOException e) {
            throw new JBomberManException("Unable to load " + path);
        }
        //return new SwingImage(Toolkit.getDefaultToolkit().getImage("l"));
    }
}
