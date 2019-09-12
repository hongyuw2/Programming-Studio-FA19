package main.model;

import main.model.pieces.King;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // Object Members
    private String name;
    private int player_no;
    private King king;
    private List<Piece> pieces;

    public Player(String name, int player_no) {
        this.name = name;
        this.player_no = player_no;
        pieces = new ArrayList<>();
    }

    public int getPlayerNo() {
        return player_no;
    }

    public void addKing(King king) {
        this.king = king;
        addPiece(king);
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public void takeAction(Board board) {

    }
}
