/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cts.chess.engine.moves;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Coin;
import org.cts.chess.engine.Coin.CoinColor;
import org.cts.chess.engine.Move;
import org.cts.chess.engine.Position;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public abstract class Castling extends Move {

    private boolean is_executed;
    protected int rank;
    protected Coin rook;

    public Castling(Coin m, Coin r, Position s, Position e, CoinColor color) {
        super(m, Coin.Empty, s, e);
        rook = r;
        rank = (color == CoinColor.WHITE) ? Position.ONE : Position.EIGHT;
    }

    @Override
    public void executeMove(ChessBoard board) {
        board.setCoinAt(moved, end);
        board.setCoinAt(rook, getRookEndPosition());
        board.setCoinAt(Coin.Empty, start);
        board.setCoinAt(Coin.Empty, getRookStartPosition());
        moved.Moved();
        rook.Moved();
        is_executed = true;
    }

    public abstract Position getRookStartPosition();

    public final Position getKingStartPosition() {
        return Position.getPosition(rank, Position.E);
    }

    public static Position getKingPosition(CoinColor color) {
        return Position.getPosition((color == CoinColor.WHITE) ? Position.ONE : Position.EIGHT, Position.E);
    }

    public abstract Position getKingEndPosition();

    @Override
    public void undoMove(ChessBoard board) {
        board.setCoinAt(moved, start);
        board.setCoinAt(rook, getRookStartPosition());
        board.setCoinAt(Coin.Empty, end);
        board.setCoinAt(Coin.Empty, getRookEndPosition());
        moved.MoveUndoved();
        rook.MoveUndoved();
        is_executed = false;
    }

    public abstract Position getRookEndPosition();

    @Override
    public Position getCapturedCoinPosition() {
        return null;
    }

    @Override
    public boolean isExecuted() {
        return is_executed;
    }

    @Override
    public double getMovePoints() {
        return 0;
    }
}
