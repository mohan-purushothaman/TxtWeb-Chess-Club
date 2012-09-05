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
import org.cts.chess.engine.board.coins.Pawn;
import org.cts.chess.engine.board.coins.black.BlackBishop;
import org.cts.chess.engine.board.coins.black.BlackKnight;
import org.cts.chess.engine.board.coins.black.BlackQueen;
import org.cts.chess.engine.board.coins.black.BlackRook;
import org.cts.chess.engine.board.coins.white.WhiteBishop;
import org.cts.chess.engine.board.coins.white.WhiteKnight;
import org.cts.chess.engine.board.coins.white.WhiteQueen;
import org.cts.chess.engine.board.coins.white.WhiteRook;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public class Promote extends Move {

    public static final int QUEEN = 9, ROOK = 5, KNIGHT = 3, BISHOP = 4, NOT_DEFINED = -10;

    public static boolean isValid(Position start, Position temp, ChessBoard board) {
        Coin c = board.getCoinAt(start);
        int tem = (c.getColor() == CoinColor.WHITE) ? 1 : -1;
        if (c instanceof Pawn && ((Pawn) c).readyForPromote()) {
            if (start.getFile() == temp.getFile()) {
                return (start.getRank() + tem == (temp.getRank())) && board.getCoinAt(temp) == Coin.Empty;
            } else if (start.getFile() == temp.getFile() - 1) {
                return (start.getRank() + tem == (temp.getRank())) && board.getCoinAt(temp) != Coin.Empty;
            } else if (start.getFile() == temp.getFile() + 1) {
                return (start.getRank() + tem == (temp.getRank())) && board.getCoinAt(temp) != Coin.Empty;
            }
            return false;
        }
        return false;
    }
    private boolean is_executed;
    private int option;
    private CoinColor color;
    private Coin promoted_coin;

    public Promote(ChessBoard board, Position start, Position end, int option, CoinColor color) {
        super(board.getCoinAt(start), board.getCoinAt(end), start, end);
        this.option = option;
        this.color = color;
    }

    @Override
    public void executeMove(ChessBoard board) {
        promoted_coin = null;

        if (option == NOT_DEFINED) {
//            PromotionDialog temp = new PromotionDialog(null, true);
//            temp.setVisible(true);
//            option = temp.getOption();
        }
        switch (option) {
            case QUEEN: {
                promoted_coin = (color == CoinColor.WHITE) ? new WhiteQueen() : new BlackQueen();
                break;
            }
            case ROOK: {
                promoted_coin = (color == CoinColor.WHITE) ? new WhiteRook() : new BlackRook();
                break;
            }
            case KNIGHT: {
                promoted_coin = (color == CoinColor.WHITE) ? new WhiteKnight() : new BlackKnight();
                break;
            }
            case BISHOP: {
                promoted_coin = (color == CoinColor.WHITE) ? new WhiteBishop() : new BlackBishop();
                break;
            }
        }
        board.setCoinAt(Coin.Empty, start);
        board.setCoinAt(promoted_coin, end);
        moved.Moved();
        promoted_coin.Moved();
    }

    @Override
    public void undoMove(ChessBoard board) {
        board.setCoinAt(moved, start);
        board.setCoinAt(captured, end);
        moved.MoveUndoved();
    }

    @Override
    public Position getCapturedCoinPosition() {
        return getEndPosition();
    }

    @Override
    public boolean isExecuted() {
        return is_executed;
    }

    @Override
    public double getMovePoints() {
        return (option == NOT_DEFINED) ? QUEEN : option;
    }

    @Override
    public String getName() {
        return "Promotion";
    }

    @Override
    public String createSAN(ChessBoard board) {
        return getStartCoinNotation() + getStartPositionNotation(board) + getCaptureNotation() + getEndPositionNotation() + "=" + getOptionChar(option);
    }

    private char getOptionChar(int op) {
        switch (op) {
            case QUEEN:
                return 'Q';
            case BISHOP:
                return 'B';
            case KNIGHT:
                return 'N';
            case ROOK:
                return 'R';
            default:
                return '?';
        }
    }

    public static int getOption(char c) {
        switch (c) {
            case 'Q':
                return QUEEN;
            case 'R':
                return ROOK;
            case 'B':
                return BISHOP;
            case 'N':
                return KNIGHT;
            default:
                return NOT_DEFINED;
        }
    }
}
