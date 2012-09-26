package org.cts.chess.txtchess.api;

import java.util.Arrays;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Move;

/**
 *	Base builder class for building different chess boards, to support different devices
 *	@author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public abstract class AbstractTxtChessBuilder {
	public String getName()
	{
		return getClass().getSimpleName();
	}

	public abstract String getDescription();

	public abstract String getChessCoinString(Character coin);

	public abstract String getRankStart(int rank);

	public abstract String getRankEnd(int rank);

	public abstract String getHeader();

	public abstract String getHeaderAndFooterSpace();

	public abstract String getFooter();

	public String getChessBoard(ChessBoard chessboard) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("<br/>");
		sb.append(getHeaderAndFooterSpace()).append(getHeader());
		Character[][] board = getChessBoardArray(chessboard.getFEN());
		for (int rank = 7; rank > -1; rank--) {
			sb.append(getRankStart(rank+1));
			for (int file = 0; file < 8; file++) {
				sb.append(getChessCoinString(board[rank][file]));
			}
			sb.append(getRankEnd(rank+1));
		}
		sb.append(getFooter());
		sb.append("<br/>").append(getMovelist(chessboard));
		return sb.toString();
	}

	private String getMovelist(ChessBoard board) {
		StringBuilder builder=new StringBuilder();
		boolean isWhite=true;
		int count=1;
		for(Move m:board.getMoves())
		{
			if(isWhite)
			{
				builder.append(count).append('.');
			}
			builder.append(' ').append(m.getSAN(board));
			if(!isWhite)
			{
				builder.append("<br/>");
				count++;
			}
			isWhite=!isWhite;
		}
		return builder.append("<br/>").toString();
	}

	private Character[][] getChessBoardArray(String FEN) {
		Character[][] board = new Character[8][8];
		for(Character[] c:board){
			Arrays.fill(c, ' ');
		}
		int rank = 7;
		int file = 0;
		for (int i = 0; i < FEN.length(); i++) {
			char c = FEN.charAt(i);
			if (c == ' ') {
				break;
			} else if (c == '/') {
				rank--;
			} else if ((c >= '0' && c <= '9')) {
				for (int j = c - '0'; j > 0; j--) {
					board[rank][file % 8] = ' ';
					file++;
				}
			} else {
				board[rank][file % 8] = c;
				file++;
			}
		}
		return board;
	}
}
