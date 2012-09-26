package org.cts.chess.txtchess.api.txtboards;

/**
 * See getDescription Method
 *@author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */

public class UnicodeImageChessBoard3 extends UnicodeImageChessBoard2 {
	
	@Override
	public String getRankEnd(int rank) {
		return "";
	}

	
	
	@Override
	public String getHeaderAndFooterSpace() {
		
		return "";
	}
	
	public String getHeader()
	{
		return "<br/>";
	}

	@Override
	public String getFooter() {
		
		return super.getHeader();
	}

	
	
	@Override
	public String getName() {
		return "Compact Unicode image ChessBoard V3 ( for High-End mobile )";
	}

	@Override
	public String getDescription() {
		return "Compact Unicode Image ChessBoard suitable for highend mobile having full unicode support";
	}

}
