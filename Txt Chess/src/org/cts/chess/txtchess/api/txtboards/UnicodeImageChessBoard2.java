package org.cts.chess.txtchess.api.txtboards;

/**
 * See getDescription Method
 *@author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */

public class UnicodeImageChessBoard2 extends UnicodeImageChessBoard {
	

	@Override
	public String getRankStart(int rank) {
		return "&#93"+(12+rank)+";";
	}

	@Override
	public String getRankEnd(int rank) {
		return getRankStart(rank)+"<br/>";
	}

	@Override
	public String getHeader() {
		return getEmspace()+"&#9398;&#9399;&#9400;&#9401;&#9402;&#9403;&#9404;&#9405;<br/>";
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
		return "Unicode image ChessBoard V2 ( for High-End mobile )";
	}

	@Override
	public String getDescription() {
		return "Unicode Image ChessBoard suitable for highend mobile having full unicode support";
	}

}
