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
public class KingSideCastling extends Castling {

    public KingSideCastling(Coin m, Coin r, Position start, Position end,CoinColor color) {
        super(m, r, start, end, color);
    }

    @Override
    public Position getRookStartPosition() {
        return Position.getPosition(rank, Position.H);
    }

    public static Position getRookPosition(CoinColor color) {
        return Position.getPosition((color == CoinColor.WHITE) ? Position.ONE : Position.EIGHT, Position.H);
    }

    @Override
    public Position getKingEndPosition() {
        return Position.getPosition(rank, Position.G);
    }

    public static Position getKingEndingPosition(CoinColor color) {
        return Position.getPosition((color == CoinColor.WHITE) ? Position.ONE : Position.EIGHT, Position.G);
    }

    @Override
    public Position getRookEndPosition() {
        return Position.getPosition(rank, Position.F);
    }

    @Override
    public String getName() {
        return "King Side Castling";
    }

    @Override
    public String createSAN(ChessBoard board) {
        return "O-O";
    }
}
