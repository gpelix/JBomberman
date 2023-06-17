package app.jbomberman.images;

/**
 * AbstractImageBuild is an abstract class that must construct images.
 *
 * @author gp
 */
public abstract class AbstractImageBuilder
{
    /**
     * getImage is the method that must be called to get the image named PATH
     * that will be located in the images/ folder in the resources
     *
     * @param path the path to the image in the images/ folder
     * @return the AbstractImage built from image/path
     */
    public abstract AbstractImage getImage(String path);
}
