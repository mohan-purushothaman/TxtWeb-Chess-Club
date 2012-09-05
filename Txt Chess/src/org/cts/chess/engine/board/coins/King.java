/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cts.chess.engine.board.coins;

import java.util.LinkedList;
import java.util.List;
import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Coin;
import org.cts.chess.engine.Move;
import org.cts.chess.engine.Position;
import org.cts.chess.engine.moves.KingSideCastling;
import org.cts.chess.engine.moves.QueenSideCastling;
import org.cts.chess.engine.moves.StandardMove;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public abstract class King extends Coin {

    private int[] y = {1, 1, 1, 0, 0, -1, -1, -1};
    private int[] x = {1, 0, -1, 1, -1, 1, 0, -1};

    @Override
    public List<Move> getMoves(ChessBoard board) {
        Position tem;
        Coin c;
        LinkedList<Move> p = new LinkedList<Move>();
        for (int i = 0; i < 8; i++) {
            tem = Position.getPosition(getRank() + x[i], getFile() + y[i]);
            c = board.getCoinAt(tem);
            /*
             * c.getColor() != getColor()) 
             * it includes the condition c==Empty or opposite coin
             * 
             */
            if (c != null && (c.getColor() != getColor())) {
                p.add(new StandardMove(board, getPosition(), tem));
            }
        }
        if (isNotMovedYet()) {
            if (canKingSideCastle(board)) {
                KingSideCastling k = new KingSideCastling(this,
                        board.getCoinAt(KingSideCastling.getRookPosition(getColor())),
                        getPosition(),
                        KingSideCastling.getKingEndingPosition(getColor()), getColor());
                //if (!(board.isHasThreat(k.getKingEndPosition(), getOpponentColor(this)) || board.isHasThreat(k.getRookEndPosition(), getOpponentColor(this)))) {
                    p.add(k);
                //}
            }
            if (canQueenSideCastle(board)) {
                QueenSideCastling q = new QueenSideCastling(this,
                        board.getCoinAt(QueenSideCastling.getRookPosition(getColor())),
                        getPosition(),
                        QueenSideCastling.getKingEndingPosition(getColor()), getColor());
                //if (!(board.isHasThreat(q.getKingEndPosition(), getOpponentColor(this)) || board.isHasThreat(q.getRookEndPosition(), getOpponentColor(this)))) {
                    p.add(q);
                //}
            }
        }
        return p;
    }

    @Override
    public double getPoints() {
        return 101;
    }

    @Override
    public CoinType getType() {
        return CoinType.KING;
    }

    private boolean canKingSideCastle(ChessBoard board) {
        Coin c = board.getCoinAt(Position.getPosition(getRank(), Position.H));

        return (c instanceof Rook && c.isNotMovedYet()
                && board.getCoinAt(Position.getPosition(getRank(), Position.F)) == Coin.Empty && board.getCoinAt(Position.getPosition(getRank(), Position.G)) == Coin.Empty);
    }

    private boolean canQueenSideCastle(ChessBoard board) {
        Coin c = board.getCoinAt(Position.getPosition(getRank(), Position.A));

        return (c instanceof Rook && c.isNotMovedYet()
                && board.getCoinAt(Position.getPosition(getRank(), Position.B)) == Coin.Empty
                && board.getCoinAt(Position.getPosition(getRank(), Position.C)) == Coin.Empty
                && board.getCoinAt(Position.getPosition(getRank(), Position.D)) == Coin.Empty);
    }

    @Override
    public char getNotation() {
        return 'K';
    }
}
