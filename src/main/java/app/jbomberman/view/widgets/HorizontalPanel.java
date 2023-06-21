package app.jbomberman.view.widgets;

import app.jbomberman.drawing.Drawing;

/**
 * HorizontalPanel is simply a panel with arrangeHorizontally as arrangementPolicy
 *
 * @param <T> the type of the element in the HorizontalPanel
 */
public class HorizontalPanel<T extends Text> extends Panel<T>
{
	/**
	 * width is the width of the horizontal panel
	 */
	private int width;

	/**
	 * Constructor from varargs
	 *
	 * @param entries the list of the texts
	 */
	@SafeVarargs
	public HorizontalPanel(T... entries)
	{
		super(entries);
		width = 0;
	}

	/**
	 * getDrawing() is a function that gets the current drawing
	 *
	 * @return the current drawing depicting the menu
	 */
	@Override
	public Drawing getDrawing()
	{
		return arrangeHorizontally();
	}

	/**
	 * getWidth() returns the width of the horizontal menu
	 *
	 * @return the width of the horizontal menu
	 */
	@Override
	public int getWidth()
	{
		return width;
	}

	/**
	 * setWidth() sets the width of the horizonral menu
	 *
	 * @param width the width of the menu
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
