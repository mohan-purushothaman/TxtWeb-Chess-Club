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
import org.cts.chess.engine.moves.Enpassant;
import org.cts.chess.engine.moves.Promote;
import org.cts.chess.engine.moves.StandardMove;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public abstract class Pawn extends Coin {

    @Override
    public List<Move> getMoves(ChessBoard board) {
        Position tem;
        Coin c;
        boolean prev = false;
        LinkedList<Move> p = new LinkedList<Move>();
        int temp = (getColor() == CoinColor.WHITE) ? 1 : -1;
        if (readyForPromote()) {
            tem = Position.getPosition(getRank() + temp, getFile());
            if (board.getCoinAt(tem) == Empty) {
                p.add(new Promote(board, getPosition(), tem, Promote.QUEEN, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.ROOK, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.BISHOP, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.KNIGHT, getColor()));
            }
            tem = Position.getPosition(getRank() + temp, getFile() + 1);
            c = board.getCoinAt(tem);
            if (c != Empty && c != null && c.getColor() != getColor()) {
                p.add(new Promote(board, getPosition(), tem, Promote.QUEEN, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.ROOK, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.BISHOP, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.KNIGHT, getColor()));
            }
            tem = Position.getPosition(getRank() + temp, getFile() - 1);
            c = board.getCoinAt(tem);
            if (c != Empty && c != null && c.getColor() != getColor()) {
                p.add(new Promote(board, getPosition(), tem, Promote.QUEEN, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.ROOK, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.BISHOP, getColor()));
                p.add(new Promote(board, getPosition(), tem, Promote.KNIGHT, getColor()));
            }
        } else {
            tem = Position.getPosition(getRank() + temp, getFile());
            if (board.getCoinAt(tem) == Empty) {
                p.add(new StandardMove(board, getPosition(), tem));
                prev = true;
            }
            tem = Position.getPosition(getRank() + temp, getFile() + 1);
            c = board.getCoinAt(tem);
            if (c != Empty && c != null && c.getColor() != getColor()) {
                p.add(new StandardMove(board, getPosition(), tem));
            }
            tem = Position.getPosition(getRank() + temp, getFile() - 1);
            c = board.getCoinAt(tem);
            if (c != Empty && c != null && c.getColor() != getColor()) {
                p.add(new StandardMove(board, getPosition(), tem));
            }
            if (prev && isNotYetMoved()) {
                tem = Position.getPosition(getRank() + temp + temp, getFile());
                if (board.getCoinAt(tem) == Empty) {
                    p.add(new StandardMove(board, getPosition(), tem));
                }
            } else if (canEnpassent()) {
                prev = false;
                tem = Position.getPosition(getRank(), getFile() - 1);
                c = board.getCoinAt(tem);
                if (c != null && c != Empty && c.getColor() != getColor()) {
                    if (board.getMoveAt(board.moveSize() - 1).equals(new StandardMove(board, Position.getPosition(getRank() + temp + temp, getFile() - 1), tem))) {
                        p.add(new Enpassant(board, getPosition(), Position.getPosition(getRank() + temp, getFile() - 1)));
                        prev = true;
                    }
                }
                tem = Position.getPosition(getRank(), getFile() + 1);
                c = board.getCoinAt(tem);
                if (c != null && c != Empty && c.getColor() != getColor() && (!prev)) {
                    if (board.getMoveAt(board.moveSize() - 1).equals(new StandardMove(board, Position.getPosition(getRank() + temp + temp, getFile() + 1), tem))) {
                        p.add(new Enpassant(board, getPosition(), Position.getPosition(getRank() + temp, getFile() + 1)));
                        prev = true;
                    }
                }
            }

        }
        return p;
    }

    @Override
    public double getPoints() {
        return 1;
    }

    @Override
    public CoinType getType() {
        return CoinType.PAWN;
    }

//    @Override
//    public void paintCoin(Graphics g, int x, int y, int width, int height) {
//        g.setColor(getOuterLayerColor());
//        g.drawOval(x + (3 * width / 8), y + height / 8, width / 4, height / 4);
//        Polygon p = new Polygon(new int[]{x + width / 2, x + width / 4, x + width - width / 4}, new int[]{y + 5 * height / 16, y + height - height / 8, y + height - height / 8}, 3);
//        g.drawPolygon(p);
//        g.setColor(getCoinColor());
//        g.fillOval(x + (3 * width / 8), y + height / 8, width / 4, height / 4);
//        g.fillPolygon(p);
//    }
    public abstract boolean isNotYetMoved();

    public abstract boolean canEnpassent();

    public abstract boolean readyForPromote();

    @Override
    public char getNotation() {
        return 'P';
    }
}
