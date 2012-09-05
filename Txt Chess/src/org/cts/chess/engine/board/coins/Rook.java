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
public abstract class Rook extends Coin {

    private Coin c;
    private Position temp;
    private boolean can_continue;
    private int i,  j;

    @Override
    public List<Move> getMoves(ChessBoard board) {
        LinkedList<Move> p = new LinkedList<Move>();
        crossMove(board, p, -1, 0);
        crossMove(board, p, 1, 0);
        crossMove(board, p, 0, -1);
        crossMove(board, p, 0, 1);
        return p;
    }

    @Override
    public double getPoints() {
        return 5;
    }

    @Override
    public CoinType getType() {
        return CoinType.ROOK;
    }

    private void crossMove(ChessBoard board, LinkedList<Move> p, int temp1, int temp2) {
        i = j = 0;
        can_continue = true;
        while (can_continue) {
            i = i + temp1;
            j = j + temp2;
            temp = Position.getPosition(getRank() + i, getFile() + j);
            c = board.getCoinAt(temp);
            if (c != null) {
                if (c.getColor() != getColor()) {
                    p.add(new StandardMove(board, getPosition(), temp));
                    if (c != Empty) {
                        can_continue = false;
                    }
                } else {
                    can_continue = false;
                }
            } else {
                can_continue = false;
            }
        }
    }

    @Override
    public char getNotation() {
        return 'R';
    }
}
