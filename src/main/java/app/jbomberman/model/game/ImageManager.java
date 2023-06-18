package app.jbomberman.model.game;

import app.jbomberman.images.AbstractImage;

import java.util.List;

public class ImageManager
{
    private static final int DEFAULT_UPDATE_INTERVAL = 10; // 60 means 1 change per sec

    private int updateInterval;

    private int timeToUpdate = 0;

    private int idxInImageViews;

    private boolean hasDoneOneCycle;

    private List<AbstractImage> currentImages;

    public ImageManager(List<AbstractImage> currentImages, int idxInImageViews)
    {
        this.currentImages = currentImages;
        this.idxInImageViews = idxInImageViews;
        this.hasDoneOneCycle = false;
        updateInterval = DEFAULT_UPDATE_INTERVAL;
    }

    public void setUpdateInterval(int updateInterval)
    {
        this.updateInterval = updateInterval;
    }

    public AbstractImage getNextImage()
    {
        AbstractImage ret = getCurrentImage();
        if (--timeToUpdate < 0) {
            if (++idxInImageViews == currentImages.size()) {
                hasDoneOneCycle = true;
                idxInImageViews = 0;
            }
            timeToUpdate = updateInterval;
        }

        return ret;
    }

    public boolean hasChanged()
    {
        return timeToUpdate == updateInterval;
    }

    public void setCurrentImages(List<AbstractImage> currentImages)
    {
        this.currentImages = currentImages;
    }

    public void setIdxInImageViews(int idxInImageViews)
    {
        this.idxInImageViews = idxInImageViews;
    }

    public int getIdxInImageViews()
    {
        return idxInImageViews;
    }

    public List<AbstractImage> getCurrentImages()
    {
        return currentImages;
    }

    public AbstractImage getCurrentImage()
    {
        return currentImages.get(idxInImageViews);
    }

    public boolean hasDoneOneCycle()
    {
        return hasDoneOneCycle;
    }
}