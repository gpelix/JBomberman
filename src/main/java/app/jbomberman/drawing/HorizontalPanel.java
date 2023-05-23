
package app.jbomberman.drawing;

public class HorizontalPanel<T extends Text> extends Panel<T>
{
	private int width;

	@SafeVarargs
	public HorizontalPanel(T... entries)
	{
		super(entries);
		width = 0;
	}

	@Override
	public Drawing getDrawing()
	{
		return arrangeHorizontally();
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}
}
