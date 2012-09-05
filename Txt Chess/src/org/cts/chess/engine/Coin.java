/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cts.chess.engine;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public abstract class Coin {

    public enum CoinColor {

        WHITE, BLACK, NONE
    }

    public enum CoinType {

        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, EMPTY
    }
   
    /**
     * Position of this coin in ChessBoard
     */
    protected Position position;
    /**
     * A empty coin which represent empty space in chessboard
     */
    public static final Coin Empty = new Coin() {
        @Override
        public double getPoints() {
            return 0;
        }

        @Override
        public List<Move> getMoves(ChessBoard board) {
            return Collections.emptyList();
        }

        @Override
        public char getNotation() {
            return ' ';
        }

        @Override
        public CoinType getType() {
            return CoinType.EMPTY;
        }

        @Override
        public CoinColor getColor() {
            return CoinColor.NONE;
        }

	
    };

    public static CoinColor getOpponentColor(Coin c) {
        return (c.getColor() == CoinColor.WHITE) ? CoinColor.BLACK : CoinColor.WHITE;
    }
    /**
     * represent number of time this coin is moved
     */
    private int move_count = 0;

    public Coin() {
    }

    /**
     * To get current position of this coin in chessboard
     *
     * @return current position of this coin in chessboard
     */
    public Position getPosition() {
        return position;
    }

    public int getRank() {
        return position.getRank();
    }

    public int getFile() {
        return position.getFile();
    }

    /**
     * Check whether this coin can move to given position on given chessboard
     *
     * @param pos Position to check
     * @param board Chessboard of this coin
     * @return if this coin can move to given position returns true ,false
     * otherwise
     */
    public boolean isHasMoveToPosition(Position pos, ChessBoard board) {
        Iterator<Move> it = getPossibleMoves(board).iterator();
        while (it.hasNext()) {
            if (it.next().getEndPosition() == pos) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set this coin position
     *
     * @param position Position of this coin in board
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    public final List<Move> getPossibleMoves(ChessBoard board) {
        List<Move> possibleMoves = new LinkedList<Move>();
        for (Move e : getMoves(board)) {
            e.executeMove(board);
            if (board.checkKingIsSafe(e.getMovedCoin().getColor())) {
                possibleMoves.add(e);
            }
            e.undoMove(board);

        }
        return possibleMoves;
    }

    /**
     * To get the moves which can be made by this coin
     *
     * @param board ChessBoard of this coin
     * @return List of moves can be performed by this coin on given Board
     */
    @Deprecated
    protected abstract List<Move> getMoves(ChessBoard board);

    /**
     * Points of this coin
     *
     * @return points of this coin
     */
    public abstract double getPoints();

    /**
     * Type of this coin (King or kinght or ...)
     *
     * @return int represent type of this coin
     */
    public abstract CoinType getType();

    /**
     * To get Color of this Coin
     *
     * @return int represent color of this coin
     */
    public abstract CoinColor getColor();

    /**
     * call if this coin is moved
     */
    public void Moved() {
        move_count++;
    }

    /**
     * call if this coin's move is undoed
     */
    public void MoveUndoved() {
        move_count--;
    }

    /**
     * To check whether it is not moved yet
     *
     * @return true if it is moved ,false otherwise
     */
    public boolean isNotMovedYet() {
        return move_count == 0;
    }

    /**
     * Notation used for represent this coin(K-KING ,N-KNIGHT, Q-QUEEN ,B-BISHOP
     * ,P-PAWN,R-ROOK)
     *
     * @return character notation of this coin
     */
    public abstract char getNotation();
    
}
