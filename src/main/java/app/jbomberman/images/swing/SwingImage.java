package app.jbomberman.images.swing;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Vector2D;

import java.awt.image.BufferedImage;

public class SwingImage extends AbstractImage
{
    private final BufferedImage image;

    public SwingImage(BufferedImage image)
    {
        this.image = image;
    }

    public BufferedImage unwrap()
    {
        return image;
    }

    @Override
    public Vector2D getDimensions()
    {
        return Vector2D.of(image.getWidth(null), image.getHeight(null));
    }

}
