package app.jbomberman.images.javafx;

import app.jbomberman.images.AbstractImage;
import app.jbomberman.util.Vector2D;
import javafx.scene.image.Image;

public class JavaFXImage extends AbstractImage
{
    private final Image image;

    public JavaFXImage(Image image)
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
        return Vector2D.of((int) image.getWidth(), (int) image.getHeight());
    }
}
