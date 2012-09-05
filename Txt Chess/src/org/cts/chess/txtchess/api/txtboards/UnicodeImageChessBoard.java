package org.cts.chess.txtchess.api.txtboards;

import java.util.Arrays;
import java.util.List;

import org.cts.chess.txtchess.api.AbstractTxtChessBuilder;

public class UnicodeImageChessBoard extends AbstractTxtChessBuilder {
	private final static List<Character> charArray=Arrays.asList(new Character[]{'K','Q','R','B','N','P','k','q','r','b','n','p'});
	
	@Override
	public String getChessCoinString(Character coin) {
		if(coin==' ')
			return getEmspace();
			return "&#98"+(12+charArray.indexOf(coin))+";";
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
		return getEmspace()+"A B C D E F G H<br/>";
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
		return "Unicode image ChessBoard( for High-End mobile )";
	}

	@Override
	public String getDescription() {
		return "Unicode Image ChessBoard suitable for highend mobile";
	}

	public String getEnspace()
	{
		return "&#8194;";
	}

	public String getEmspace()
	{
		return "&#8195;";
	}
}
