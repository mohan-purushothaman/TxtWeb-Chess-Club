package org.cts.chess.txtchess.api.txtboards;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.txtchess.api.AbstractTxtChessBuilder;
	
/**
 * See getDescription Method
 *@author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */
public class FENChessBoard extends AbstractTxtChessBuilder {

	@Override
	public String getDescription() {
		return "FEN notation of chess board";
	}

	@Override
	public String getChessCoinString(Character coin) {
		return null;
	}

	@Override
	public String getRankStart(int rank) {
		return null;
	}

	@Override
	public String getRankEnd(int rank) {
		return null;
	}

	@Override
	public String getHeader() {
		return null;
	}

	@Override
	public String getHeaderAndFooterSpace() {
		return null;
	}

	@Override
	public String getFooter() {
		return null;
	}

	@Override
	public String getName() {
		return "FEN chess board";
	}

	@Override
	public String getChessBoard(ChessBoard board) {
		return board.getFEN()+"<br/>";
	}
	
	
}
