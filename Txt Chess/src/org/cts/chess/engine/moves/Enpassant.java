/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cts.chess.engine.moves;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Coin;
import org.cts.chess.engine.Move;
import org.cts.chess.engine.Position;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public class Enpassant extends Move {

    private boolean is_executed;

    public Enpassant(ChessBoard board, Position start, Position end) {
        super(board.getCoinAt(start), board.getCoinAt(getCapturedCoinPosition(start, end)), start, end);
    }

    @Override
    public void executeMove(ChessBoard board) {
        board.setCoinAt(moved, end);
        board.setCoinAt(Coin.Empty, getCapturedCoinPosition(start, end));
        board.setCoinAt(Coin.Empty, start);
        moved.Moved();
        is_executed = true;
    }

    @Override
    public void undoMove(ChessBoard board) {
        board.setCoinAt(moved, start);
        board.setCoinAt(captured, getCapturedCoinPosition(start, end));
        board.setCoinAt(Coin.Empty, end);
        moved.MoveUndoved();
        is_executed = false;
    }

   

    @Override
    public Position getCapturedCoinPosition() {
        return Position.getPosition(start.getRank(), end.getFile());

    }

    public static Position getCapturedCoinPosition(Position s, Position e) {
        return Position.getPosition(s.getRank(), e.getFile());
    }

    @Override
    public boolean isExecuted() {
        return is_executed;
    }

    @Override
    public double getMovePoints() {
        return getCapturedCoin().getPoints();
    }

    @Override
    public String getName() {
        return "Enpassent";
    }

    @Override
    public String createSAN(ChessBoard board) {
        return getStartCoinNotation() + getStartPositionNotation(board) + getCaptureNotation() + getEndPositionNotation();
    }
}
