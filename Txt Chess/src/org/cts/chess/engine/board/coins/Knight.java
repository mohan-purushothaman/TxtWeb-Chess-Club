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
import org.cts.chess.engine.moves.StandardMove;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public abstract class Knight extends Coin {

    public static int[] x = {2, 2, 1, 1, -2, -2, -1, -1};
    public static int[] y = {1, -1, 2, -2, 1, -1, 2, -2};

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
        return p;
    }

    @Override
    public double getPoints() {
        return 3;
    }

    @Override
    public CoinType getType() {
        return CoinType.KNIGHT;
    }

    @Override
    public char getNotation() {
        return 'N';
    }
}
