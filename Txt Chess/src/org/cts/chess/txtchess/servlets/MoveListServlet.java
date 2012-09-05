package org.cts.chess.txtchess.servlets;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Coin;
import org.cts.chess.engine.Move;
import org.cts.chess.txtchess.api.ListIterator;
import org.cts.chess.txtchess.api.TxtWebServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.Game;

@SuppressWarnings("serial")
public class MoveListServlet extends TxtWebServlet{

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message)
			throws Exception {
		
		EntityManager manager = EMF.get().createEntityManager();
		try {
		final Long gameId=Long.parseLong(request.getParameter("gameId"));
		Game game=manager.find(Game.class, gameId);
		
		if(!game.getCurrentPlayer().equals(mobileHash))
		{
			throw new Exception("It is not your turn to move, try again after opponent makes a move");
		}
		
		final ChessBoard board=game.reCreateBoard();
		List<Move> allMoves=new LinkedList<Move>();
		Iterator<Coin> it=board.getCoinIterator();
				while(it.hasNext())
				{
					Coin c=it.next();
					if(c!=null&c!=Coin.Empty&&c.getColor()==board.getCurrentPlayerColor())
					{
						allMoves.addAll(c.getPossibleMoves(board));
					}
				}
		String startIndexString=request.getParameter("startIndex");
		
		request.setAttribute("__listIterator", new ListIterator<Move>("Moves",allMoves,startIndexString==null?1:Integer.parseInt(startIndexString),"/listMoves?gameId="+gameId+"&txtweb-message="+(message==null?"":message)+"&","")
				{

					@Override
					public String getRowLink(Move move) {
						return "<a href='./moveServlet?gameId="+gameId+"&txtweb-message="+move.getSAN(board)+"'>"+move.getInfo(board)+"</a>";
					}
			
				}
		); 
		setJspPath("/jsp/common/ListIteratorHandler.jsp");
		}
		finally
		{
			manager.close();
		}
	}

}
