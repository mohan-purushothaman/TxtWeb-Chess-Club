package org.cts.chess.txtchess.api.txtboards;

import org.cts.chess.txtchess.api.AbstractTxtChessBuilder;

public class UnicodeTxtChessBoard extends AbstractTxtChessBuilder {
	
	@Override
	public String getChessCoinString(Character coin){
		if(coin==' ')
			return getEmspace();
		if('A'<=coin&&'Z'>=coin)
		{
			return "&#00"+(65+(coin-'A'))+";";
		}
		if('a'<=coin&&'z'>=coin)
		{
		return "&#00"+(97+(coin-'a'))+";";
		}
		throw new RuntimeException("No Coin Exist in character "+coin);
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
		return getEmspace()+"A"+getEnspace()+"B"+getEnspace()+"C"+getEnspace()+"D"+getEnspace()+"E"+getEnspace()+"F"+getEnspace()+"G"+getEnspace()+"H<br/>";
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
		return "Unicode text ChessBoard( for Middle-End mobile )";
	}

	@Override
	public String getDescription() {
		return "Unicode text ChessBoard suitable for Middle-End mobile";
	}
	
	private String getEnspace()
	{
		return "&#8194;";
	}

	private String getEmspace()
	{
		return "&#8195;";
	}
}
