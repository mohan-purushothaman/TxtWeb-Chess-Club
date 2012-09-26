package org.cts.chess.txtchess.api.txtboards;

import org.cts.chess.txtchess.api.AbstractTxtChessBuilder;

/**
 * See getDescription Method
 *@author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */
public class SimpleTxtChessBoard extends AbstractTxtChessBuilder {
	
	@Override
	public String getChessCoinString(Character coin) {
		if(coin==' ')
			return "&nbsp;";
		return String.valueOf(coin);
	}

	@Override
	public String getRankStart(int rank) {
		return rank+"|";
	}

	@Override
	public String getRankEnd(int rank) {
		return "|"+rank+"<br/>";
	}

	@Override
	public String getHeader() {
		return "-|ABCDEFGH<br/>";
	}

	@Override
	public String getHeaderAndFooterSpace() {
		
		return "";
	}

	@Override
	public String getFooter() {
		
		return getHeader();
	}

	
	
	@Override
	public String getName() {
		return "Simple Text ChessBoard V1( for Low-End mobile )";
	}

	@Override
	public String getDescription() {
		return "Simple Text ChessBoard suitable for low end mobile with fixed width font";
	}

}
