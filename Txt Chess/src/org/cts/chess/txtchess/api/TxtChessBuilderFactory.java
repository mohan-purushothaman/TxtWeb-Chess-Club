package org.cts.chess.txtchess.api;

import java.util.LinkedList;
import java.util.List;

import org.cts.chess.txtchess.api.txtboards.FENChessBoard;
import org.cts.chess.txtchess.api.txtboards.SimpleTxtChessBoard;
import org.cts.chess.txtchess.api.txtboards.SimpleTxtChessBoard2;
import org.cts.chess.txtchess.api.txtboards.UnicodeImageChessBoard;

/**
 * Factory class for different type of chess boards
 * 
 */

public class TxtChessBuilderFactory {
	private static final List<AbstractTxtChessBuilder> list=new LinkedList<AbstractTxtChessBuilder>();;
	static{
		list.add(new SimpleTxtChessBoard());
		list.add(new SimpleTxtChessBoard2());
		//list.add(new UnicodeTxtChessBoard());
		list.add(new UnicodeImageChessBoard());
		//list.add(new UnicodeImageChessBoard2());
		//list.add(new UnicodeImageChessBoard3());
		list.add(new FENChessBoard());
	}
	
	public static List<AbstractTxtChessBuilder> getAllInstances()
	{
		return list;
	}
	
	public static AbstractTxtChessBuilder getTxtBuilder(String className)
	{
		for(AbstractTxtChessBuilder t:list)
		{
			if(t.getClass().getName().equals(className))
			{
				return t;
			}
		}
		throw new RuntimeException("Unable to find a txt chess builder "+className);
	}
}
