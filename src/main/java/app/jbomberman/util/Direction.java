package app.jbomberman.util;

/**
 * Direction is an enum that represents the four possible cardinal points and
 * the only possible moves of an element in a grid
 *
 * @author gp
 */
public enum Direction
{
    NORTH(Vector2D.of(0, -1)),
    EAST(Vector2D.of(1, 0)),
    SOUTH(Vector2D.of(0, 1)),
    WEST(Vector2D.of(-1, 0));

    /**
     * A versor associated with the direction
     */
    private final Vector2D associatedVersor;

    /**
     * Create an instance of a Direction specifying the versor that must be
     * associated to it
     *
     * @param associatedVersor the versor that must be associated to the
     *                         enum
     */
    Direction(Vector2D associatedVersor)
    {
        this.associatedVersor = associatedVersor;
    }

    /**
     * get the versor
     *
     * @return the associated versor
     */
    public Vector2D getVersor()
    {
        return associatedVersor;
    }

    /**
     * check is the direction is EAST/WEST
     *
     * @return whether the direction is EAST/WEST
     */
    public boolean isHorizontal()
    {
        return this == EAST || this == WEST;
    }

    /**
     * check is the direction is NORTH/SOUTH
     *
     * @return whether the direction is NORTH/SOUTH
     */
    @SuppressWarnings("unused")
    public boolean isVertical()
    {
        return this == NORTH || this == SOUTH;
    }

    /**
     * get a direction from a vector indicating that
     *
     * @param vector a vector that points in a certain direction
     * @return a maybe object that contains the direction if the vector indicated
     * a valid direction or nothing if the vector is askew
     */
    public static Maybe<Direction> fromDirectionVector(Vector2D vector)
    {
        int x = vector.getX();
        int y = vector.getY();

        return Maybe.something(
            x > 0 ? EAST : x < 0 ? WEST : y > 0 ? SOUTH : y < 0 ? NORTH : null);
    }

    /**
     * @return the opposite direction of this
     */
    public Direction opposite()
    {
        return switch (this) {
            case EAST -> WEST;
            case WEST -> EAST;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
        };
    }
}
