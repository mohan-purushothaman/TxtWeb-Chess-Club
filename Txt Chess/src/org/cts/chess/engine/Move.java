/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cts.chess.engine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.cts.chess.engine.Coin.CoinColor;
import org.cts.chess.engine.moves.Castling;
import org.cts.chess.engine.moves.Enpassant;
import org.cts.chess.engine.moves.KingSideCastling;
import org.cts.chess.engine.moves.Promote;
import org.cts.chess.engine.moves.QueenSideCastling;
import org.cts.chess.engine.moves.StandardMove;

/**
 * Abstarct Class to represent basic moves in chessboard
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public abstract class Move {

    private int NAG;
    private String comment;
    private String san;
    public Coin moved, captured;
    public Position start, end;

    public Move(Coin moved, Coin captured, Position start, Position end) {
        this.moved = moved;
        this.captured = captured;
        this.start = start;
        this.end = end;
    }

    public int getNAG() {
        return NAG;
    }

    public void setNAG(int NAG) {
        this.NAG = NAG;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public abstract void executeMove(ChessBoard board);

    public void setSAN(String s) {
        san = s;
    }

    public abstract void undoMove(ChessBoard board);

    public final Position getStartPosition() {
        return start;
    }

    public final Position getEndPosition() {
        return end;
    }

    public final Position getMovedCoinPosition() {
        return start;
    }

    public abstract Position getCapturedCoinPosition();

    public final Coin getMovedCoin() {
        return moved;
    }

    public final Coin getCapturedCoin() {
        return captured;
    }

    public abstract boolean isExecuted();

    public abstract double getMovePoints();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Move)) {
            return false;
        }
        Move other = (Move) obj;
        return (getStartPosition() == other.getStartPosition() && getEndPosition() == other.getEndPosition() && getOptionNo() == other.getOptionNo());
    }

    public int getOptionNo() {
        return 1;
    }

    public abstract String getName();

    public final String getSAN(ChessBoard board) {
        if (san == null) {
            san = createSAN(board) + extraDetails();
        }
        return san;
    }

    public String getStartCoinNotation() {
        char c = getMovedCoin().getNotation();
        return (c == 'P') ? "" : String.valueOf(c);
    }

    public String getStartPositionNotation(ChessBoard board) {
        Iterator<Coin> it = board.getCoinIterator();
        Coin c;
        boolean temp = false;
        boolean file = false, rank = false;
        while (it.hasNext()) {
            c = it.next();
            if (c.getColor() == getMovedCoin().getColor() &&
                    c.getType() == getMovedCoin().getType() &&
                    c != getMovedCoin()) {
                if (c.isHasMoveToPosition(getEndPosition(), board)) {
                    temp = true;
                    if (c.getFile() == getStartPosition().getFile()) {
                        file = true;

                    } else if (c.getRank() == getStartPosition().getRank()) {
                        rank = true;
                    }
                }
            }
        }
        if (!temp) {
            return "";
        }
        if (file && rank) {
            return new String(new char[]{getStartPosition().getFileChar(), getStartPosition().getRankChar()});
        }
        return String.valueOf((!file) ? getStartPosition().getFileChar() : getStartPosition().getRankChar());
    }

    public String getCaptureNotation() {
        return ((getCapturedCoin() != Coin.Empty) ? "x" : "");
    }

    public String getEndPositionNotation() {
        return new String(new char[]{getEndPosition().getFileChar(), getEndPosition().getRankChar()});
    }

    public abstract String createSAN(ChessBoard board);

    private String extraDetails() {
        return "";
    }

    public static Move getMove(ChessBoard board, String san) throws Exception {
    	
        char c = san.charAt(0);
        CoinColor color = board.getCurrentPlayerColor();
        boolean promote = false;
        if (c != 'K' && c != 'Q' && c != 'R' && c != 'B' && c != 'N' && c != 'O') {
            c = 'P';
            if (san.contains("=")) {
                promote = true;
            }
        } else if (c == 'O') {
            if (san.equals("O-O")) {
                return new KingSideCastling(board.getCoinAt(Castling.getKingPosition(color)),
                        board.getCoinAt(KingSideCastling.getRookPosition(color)),
                        Castling.getKingPosition(color),
                        KingSideCastling.getKingEndingPosition(color),
                        color);
            } else {
                return new QueenSideCastling(board.getCoinAt(Castling.getKingPosition(color)),
                        board.getCoinAt(QueenSideCastling.getRookPosition(color)),
                        Castling.getKingPosition(color),
                        QueenSideCastling.getKingEndingPosition(color),
                        color);
            }
        }

        if (san.contains("-") || san.contains("*")) {
            return null;
        }
        Position end = Position.getSAN_EndPosition(san);
        Iterator<Coin> it = board.getCoinIterator();
        LinkedList<Coin> v = new LinkedList<Coin>();
        Coin coin;
        while (it.hasNext()) {
            coin = it.next();
            if (coin.getColor() == color && coin.getNotation() == c) {
                if (coin.isHasMoveToPosition(end, board)) {
                    v.add(coin);
                }
            }
        }
        coin = getCorrectCoin(board, san, v);


        if (c == 'P') {
            if (promote) {
                return new Promote(board, coin.getPosition(), end, Promote.getOption(san.charAt(san.indexOf('=') + 1)), color);
            }
           // Iterator<Move> m = coin.getPossibleMoves(board).iterator();
            if (coin.getPosition().getFile() != end.getFile() && board.getCoinAt(end) == Coin.Empty) {
                return new Enpassant(board, coin.getPosition(), end);
            }
        }
        return new StandardMove(board, coin.getPosition(), end);
    }

    private static Coin getCorrectCoin(ChessBoard board, String san, List<Coin> v) throws Exception {
        if (v.size() == 1) {
            return v.get(0);
        }
        int file = getStartingFile(san);
        int rank = 0;
        if (file != -1) {
            for (int i = 0; i < v.size(); i++) {
                if (v.get(i).getFile() != file) {
                    v.remove(i);
                }
            }
            if (v.size() == 1) {
                return v.get(0);
            }
            rank = getStartingRank(san);
            return board.getCoinAt(Position.getPosition(rank, file));
        } else {
            rank = getStartingRank(san);
        }

        for (int i = 0; i < v.size(); i++) {
            if (v.get(i).getRank() != rank) {
                v.remove(i);
            }
        }
        if (v.size() == 1) {
            return v.get(0);
        }
        throw new Exception("Not Contain valid information to represent a Move");
    }

    private static int getStartingFile(String san) {
        int max = Math.max(2, san.length());
        for (int i = 0; i < max; i++) {
            char c = san.charAt(i);
            if (c >= 'a' && c <= 'h') {
                return c - 'a';
            }
        }
        return -1;
    }

    private static int getStartingRank(String san) {
        int max = Math.max(3, san.length());
        for (int i = 0; i < max; i++) {
            char c = san.charAt(i);
            if (c >= '1' && c <= '8') {
                return c - '1';
            }
        }
        return -1;
    }

	public String getInfo(ChessBoard board) {
		return  getName()
				+ " ( "
				+ getSAN(board) + " )";
	}
}
