package org.cts.chess.txtchess.api.txtboards;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.txtchess.api.AbstractTxtChessBuilder;

public class FENChessBoard extends AbstractTxtChessBuilder {

	@Override
	public String getDescription() {
		return "FEN notation of chess board";
	}

	@Override
	public String getChessCoinString(Character coin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRankStart(int rank) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRankEnd(int rank) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHeaderAndFooterSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFooter() {
		// TODO Auto-generated method stub
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
