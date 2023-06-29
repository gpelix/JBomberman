package app.jbomberman.model.game;

import app.jbomberman.image.AbstractImage;

import java.util.List;

/**
 * ImageManager is a class that will be used to manage a set of images.
 *
 * @author gp
 */
public class ImageManager
{
	/**
	 * DEFAULT_UPDATE_INTERVAL is the default number of times the image is requested
	 * before going to the next image
	 */
	private static final int DEFAULT_UPDATE_INTERVAL = 10; // 10 means: ten requests, 1 change

	/**
	 * updateInterval is the number of times the image is requested before going
	 * to the next image
	 */
	private int updateInterval;

	/**
	 * timeToUpdate is the remaining retrievals before the current image is
	 * changed
	 */
	private int timeToUpdate = 0;

	/**
	 * idxInImages is the index of the current image in the list of images
	 */
	private int idxInImages;

	/**
	 * hasDoneOneCycle is a boolean that is true if and only if all the images
	 * have been displayed at least one time
	 */
	private boolean hasDoneOneCycle;

	/**
	 * images is the list of images
	 */
	private List<AbstractImage> images;

	/**
	 * public constructor of ImageManager from a list of Images
	 *
	 * @param images      the list of images
	 * @param idxInImages the index in the image list of the current image
	 */
	public ImageManager(List<AbstractImage> images, int idxInImages)
	{
		this.images = images;
		this.idxInImages = idxInImages;
		this.hasDoneOneCycle = false;
		updateInterval = DEFAULT_UPDATE_INTERVAL;
	}

	/**
	 * setUpdateInterval() sets the updateInterval
	 *
	 * @param updateInterval the updateInterval to be set
	 */
	public void setUpdateInterval(int updateInterval)
	{
		this.updateInterval = updateInterval;
	}

	/**
	 * getImage() gets the current image or the next one depending on the number
	 * of times the current image has been retrieved
	 *
	 * @return the current image after eventually been updated
	 */
	public AbstractImage getImage()
	{
		AbstractImage ret = getCurrentImage();
		if (--timeToUpdate < 0) {
			if (++idxInImages == images.size()) {
				hasDoneOneCycle = true;
				idxInImages = 0;
			}
			timeToUpdate = updateInterval;
		}

		return ret;
	}

	/**
	 * hasChanged() returns true if and only if, at the previous call of
	 * getImage(), an update was done
	 *
	 * @return true if and only if, at the previous call of getImage(), an update
	 * was done
	 */
	public boolean hasChanged()
	{
		return timeToUpdate == updateInterval;
	}

	/**
	 * setImages sets the list of images and resets the state of the ImageManager
	 *
	 * @param images the list of images to be set
	 */
	public void setImages(List<AbstractImage> images)
	{
		timeToUpdate = updateInterval;
		idxInImages = 0;
		this.images = images;
	}

	/**
	 * setIdxInImages() sets the index of the current image in the image list
	 *
	 * @param idxInImages the index of the current image in images
	 */
	public void setIdxInImages(int idxInImages)
	{
		if (idxInImages >= images.size())
			throw new GamePlayException("Index " + idxInImages +
				" out of Bound in images of size " + images.size());
		this.idxInImages = idxInImages;
	}

	/**
	 * getCurrentImage() must be called to get the current image without doing any
	 * update at all
	 *
	 * @return the current image
	 */
	public AbstractImage getCurrentImage()
	{
		return images.get(idxInImages);
	}

	/**
	 * hasDoneOneCycle() returns the hasDoneOneCycle flag
	 *
	 * @return the hasDoneOneCycle flag
	 */
	public boolean hasDoneOneCycle()
	{
		return hasDoneOneCycle;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
