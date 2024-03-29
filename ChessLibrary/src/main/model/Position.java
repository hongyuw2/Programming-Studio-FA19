package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Position Class integrating x and y coordinate
 * Contains basic helper functions and handles relationship between positions
 */
public class Position {
    public int x;
    public int y;

    /**
     * Constructor by given x and y coordinates
     * @param x The x coordinate of the target position
     * @param y The y coordinate of the target position
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor by decoding the given String
     * Assume coord is ranging from A1 to H8
     * @param coord The encoded String of a given position
     */
    public Position(String coord) {
        x = coord.charAt(0) - 'A';
        y = coord.charAt(1) - '1';
    }

    /**
     * Checks if this Position is equal to another object
     * @param other The other object to check equality
     * @return True if the argument is a Position object and refers to the same position
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Position)) return false;
        return x == ((Position)other).x && y == ((Position)other).y;
    }

    /**
     * Encodes the Position object into String
     * @return The String encoded by this object
     */
    @Override
    public String toString() {
        return "" + (char)('A' + x) + (1 + y);
    }

    /**
     * Judges whether the position is outside of the board
     * @return True if this position is outside of the board
     *         False otherwise
     */
    public boolean outsideOfBoard() {
        if (x < 0 || x >= Board.WIDTH) return true;
        if (y < 0 || y >= Board.HEIGHT) return true;
        return false;
    }

    /**
     * Calculates the direction from current position to destination and check if it is legal in Chess Game
     * @param dest The Position object indicating the given destination
     * @return The direction from current position to destination
     */
    public Direction getDirectionTo(Position dest) {
        // Judge if the direction is straight
        if (x == dest.x && y == dest.y) return Direction.ILLEGAL;
        else if (y == dest.y) return dest.x > x ? Direction.RIGHT : Direction.LEFT;
        else if (x == dest.x) return dest.y > y ? Direction.UP : Direction.DOWN;

        // Judge if the direction is diagonal
        int x_displacement = dest.x - x, y_displacement = dest.y - y;
        if (x_displacement == y_displacement) return x_displacement > 0 ? Direction.UP_RIGHT : Direction.DOWN_LEFT;
        else if (x_displacement == - y_displacement) return x_displacement > 0 ? Direction.DOWN_RIGHT : Direction.UP_LEFT;

        // Judge if the direction is special for Knight
        if (Math.abs(x_displacement) + Math.abs(y_displacement) == 3) return Direction.KNIGHT;

        // Illegal Otherwise
        return Direction.ILLEGAL;
    }

    /**
     * Calculates all positions crossed from current position to the given destination
     * @param dest The Position object indicating the given destination
     * @param dir The direction from current position to destination
     * @param inclusive True if dest is required to put in the returned list
     * @return A list of positions crossed from current position to the given destination
     */
    public List<Position> getPositionsCrossed(Position dest, Direction dir, boolean inclusive) {
        List<Position> posCrossed = new LinkedList<>();
        int x = this.x, y = this.y;
        boolean end = false;
        do {
            // Moves toward dest
            if (dir.isDownward()) y--;
            if (dir.isUpward()) y++;
            if (dir.isLeftWard()) x--;
            if (dir.isRightWard()) x++;

            if (x == dest.x && y == dest.y) { // Additional iteration if inclusive
                end = true;
                if (!inclusive) break;
            }

            posCrossed.add(new Position(x, y));
        } while (!end);
        return posCrossed;
    }
}
