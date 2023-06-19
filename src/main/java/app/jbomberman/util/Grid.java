package app.jbomberman.util;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Grid represents a grid wrapping a multidimensional array of generic type T
 * The problem with generic arrays is that we cannot instance them due to
 * template parameter elimination. So we have to wrap them into this interface
 * to provide a friendly interface to the programmer
 *
 * @param <T> generic type of objects in the grid
 * @author gp
 */
public class Grid<T> implements Iterable<T>
{
	/**
	 * GridIterator is an iterator that allows simple iteration over the 2D
	 * structure of the grid without exposing the underlying private
	 * representation
	 *
	 * @author gp
	 */
	private class GridIterator implements Iterator<T>
	{
		/**
		 * current x and y coordinates in the grid. We avoid using a
		 * Vector2D object as it would be more expensive in terms of
		 * preformances
		 */
		private int x, y;

		/**
		 * private constructor that must only be called from the iterator()
		 * function of Grid. We don't allow construction of iterators outside
		 * Grid for obvious reasons
		 */
		private GridIterator()
		{
			x = 0;
			y = 0;
		}

		/**
		 * Check is there is still an element to return
		 *
		 * @return whether or not we have iterated on the whole grid
		 */
		@Override
		public boolean hasNext()
		{
			return y < height;
		}

		/**
		 * of next element in the grid
		 *
		 * @return the next element in the grid
		 */
		@Override
		public T next()
		{
			T ret = get(y, x++);
			if (x == width) {
				x = 0;
				y++;
			}
			return ret;
		}
	}

	/**
	 * width and height fields of the grid
	 */
	private final int width, height;

	/**
	 * Components of the grid
	 */
	private final Object[][] grid;

	/**
	 * construct a grid from width and height
	 *
	 * @param width  the width of the grid
	 * @param height the height of the grid
	 */
	public Grid(int width, int height)
	{
		this.height = height;
		this.width = width;
		grid = new Object[height][width];
	}

	public Grid(Vector2D dimensions)
	{
		this(dimensions.getX(), dimensions.getY());
	}

	/**
	 * of an element from the grid without the need of using a coordinate
	 * object. This is what happens when we don't want to add the overhead of
	 * a Vector2D like when we access the grid from a GridIterator
	 *
	 * @param y the y coordinate of the object to of in the grid
	 * @param x the x coordinate of the object to of in the grid
	 * @return the element at the position specified by coordinate
	 */
	// Suppress the warning as we can only set elements using set method
	// that requires a T obj
	@SuppressWarnings("unchecked")
	private T get(int y, int x)
	{
		checkInBounds(Vector2D.of(x, y));
		return (T) grid[y][x];
	}

	/**
	 * of element from the grid given a coordinate
	 *
	 * @param coordinate coordinate specifying the position of the element to
	 *				   return
	 * @return the element at the position specified by coordinate
	 */
	@SuppressWarnings("unchecked")
	public T get(Vector2D coordinate)
	{
		checkInBounds(coordinate);
		return (T) grid[coordinate.getY()][coordinate.getX()];
	}

	/**
	 * set() must be used to insert an element into the grid
	 *
	 * @param obj		the object to insert
	 * @param coordinate the coordinate at which the object must be inserted
	 */
	public void set(T obj, Vector2D coordinate)
	{
		checkInBounds(coordinate);
		grid[coordinate.getY()][coordinate.getX()] = obj;
	}


	/**
	 * of the width of the grid
	 *
	 * @return the width of the grid
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * of the height of the grid
	 *
	 * @return the height of the grid
	 */
	public int getHeight()
	{
		return height;
	}

	public Vector2D getDimensions()
	{
		return Vector2D.of(width, height);
	}


	/**
	 * checkInBounds is useful only during the debug stage. Whenever there is a
	 * of/set operation on an element of the table, this function is called to
	 * and checks whether the specified coordinates are within the bounds of the
	 * grid or not
	 *
	 * @param c the coordinate to check
	 */
	public void checkInBounds(Vector2D c)
	{
		if (!isWithinBoundaries(c))
			throw new RuntimeException("Out of bound access. Trying to access to object with" +
				" coordinates" +
				"(" + c.getX() + ", " + c.getY() + ") but the grid has " +
				"width=" + width + ", height=" + height);
	}

	/**
	 * Check whether a place in the grid is empty or not
	 *
	 * @param coordinate the coordinate of the place
	 * @return a boolean that says whether the place at coordinate is empty or
	 * not
	 */
	public boolean isEmpty(Vector2D coordinate)
	{
		return get(coordinate) == null;
	}

	/**
	 * isWithinBoundaries is the backbone of checkInBounds and checks if a
	 * coordinate indicates a valid place in the grid
	 *
	 * @param coordinate the coordinate of the place
	 * @return whether the coordinate indicates a valid place in the grid
	 */
	public boolean isWithinBoundaries(Vector2D coordinate)
	{
		return coordinate.getX() >= 0 && coordinate.getY() >= 0 &&
			coordinate.getX() < width && coordinate.getY() < height;
	}

	/**
	 * @return a GridIterator over the Grid
	 */
	@Override
	public Iterator<T> iterator()
	{
		return new GridIterator();
	}

	/**
	 * getNeighborsCoordinates is a method that returns the neighbors of a coordinate
	 * checking that they are within boundaries
	 *
	 * @param coordinate the coordinate whose neighbors we want to know about
	 * @return a stream containing the neighbors of the coordinate which are within boundaries
	 */
	public Stream<Vector2D> getNeighborsCoordinates(Vector2D coordinate)
	{
		return Stream.of(
				coordinate.sum(Vector2D.of(0, 1)),
				coordinate.sum(Vector2D.of(0, -1)),
				coordinate.sum(Vector2D.of(1, 0)),
				coordinate.sum(Vector2D.of(-1, 0)))
			.filter(this::isWithinBoundaries);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
