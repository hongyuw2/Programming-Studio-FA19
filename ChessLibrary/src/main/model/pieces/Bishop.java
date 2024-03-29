package model.pieces;

import model.*;

import java.util.List;

/**
 * Bishop Class in the normal set of chess pieces
 */
public class Bishop extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Bishop piece
     * @param y The y coordinate of this Bishop piece
     * @param owner The player who owns this Bishop piece
     */
    public Bishop(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judges whether the Bishop piece can move to the given destination
     * The Bishop can move diagonally without leaping over other pieces
     * @param dest The destination for the Bishop piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the Bishop piece can move to dest
     *         False otherwise
     */
    @Override
    public boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied) {
        // Checks if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isDiagonal()) return false;
        checkUnoccupied.addAll(currPos.getPositionsCrossed(dest, dir, false));
        return true;
    }

    /**
     * Encodes the Bishop piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Bishop piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "B" : "b";
    }

    /**
     * Gets the full name of this piece
     * @return The full name of this piece
     */
    @Override
    public String getFullName() {
        return "Bishop";
    }
}
