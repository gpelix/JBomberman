package app.jbomberman.images.swing;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.AbstractImageBuilder;

import java.awt.Toolkit;

public class SwingImageBuilder extends AbstractImageBuilder
{
    @Override
    public AbstractImage getImage(String path)
    {
        return new SwingImage(Toolkit.getDefaultToolkit().getImage(path));
    }
}
