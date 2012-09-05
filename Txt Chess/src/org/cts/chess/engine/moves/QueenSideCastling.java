/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cts.chess.engine.moves;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Coin;
import org.cts.chess.engine.Coin.CoinColor;
import org.cts.chess.engine.Position;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public class QueenSideCastling extends Castling {

    public QueenSideCastling(Coin m, Coin r, Position start, Position end, CoinColor color) {
        super(m, r, start, end, color);
    }

    @Override
    public Position getRookStartPosition() {
        return Position.getPosition(rank, Position.A);
    }

    public static Position getRookPosition(CoinColor color) {
        return Position.getPosition((color == CoinColor.WHITE) ? Position.ONE : Position.EIGHT, Position.A);
    }

    @Override
    public Position getKingEndPosition() {
        return Position.getPosition(rank, Position.C);
    }

    public static Position getKingEndingPosition(CoinColor color) {
        return Position.getPosition((color == CoinColor.WHITE) ? Position.ONE : Position.EIGHT, Position.C);
    }

    @Override
    public Position getRookEndPosition() {
        return Position.getPosition(rank, Position.D);
    }

    @Override
    public String getName() {
        return "QueenSideCastling";
    }

    @Override
    public String createSAN(ChessBoard board) {
        return "O-O-O";
    }
}
