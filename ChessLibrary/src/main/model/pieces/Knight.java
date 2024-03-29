package model.pieces;

import model.*;

import java.util.List;

/**
 * Knight Class in the normal set of chess pieces
 */
public class Knight extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Knight piece
     * @param y The y coordinate of this Knight piece
     * @param owner The player who owns this Knight piece
     */
    public Knight(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judges whether the Knight piece can move to the given destination
     * The Knight can move in a L-shape direction
     * @param dest The destination for the Knight piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the Knight piece can move to dest
     *         False otherwise
     */
    @Override
    public boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        return dir == Direction.KNIGHT;
    }

    /**
     * Encodes the Knight piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Knight piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "N" : "n";
    }

    /**
     * Gets the full name of this piece
     * @return The full name of this piece
     */
    @Override
    public String getFullName() {
        return "Knight";
    }
}
