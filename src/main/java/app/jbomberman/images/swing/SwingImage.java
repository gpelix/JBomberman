package app.jbomberman.images.swing;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Vector2D;

import java.awt.*;

public class SwingImage extends AbstractImage
{
    private final Image image;

    public SwingImage(Image image)
    {
        this.image = image;
    }

    public Image unwrap()
    {
        return image;
    }

    @Override
    public Vector2D getDimensions()
    {
        return Vector2D.of(image.getWidth(null), image.getHeight(null));
    }

}
