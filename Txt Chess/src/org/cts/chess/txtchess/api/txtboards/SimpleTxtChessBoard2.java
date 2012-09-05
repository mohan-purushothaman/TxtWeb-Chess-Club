package org.cts.chess.txtchess.api.txtboards;


public class SimpleTxtChessBoard2 extends SimpleTxtChessBoard {
	
	@Override
	public String getChessCoinString(Character coin) {
		if(coin==' ')
			return "-";
		return super.getChessCoinString(coin);
	}
	
	@Override
	public String getName() {
		return "Simple Text ChessBoard V2 ( for Low-End mobile )";
	}

	@Override
	public String getDescription() {
		return "Simple Text ChessBoard suitable for low end mobile which will look better than V1 in variable width font";
	}

}
