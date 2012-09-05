package org.cts.chess.txtchess.servlets;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.Condition;
import org.cts.chess.txtchess.api.Parameter;
import org.cts.chess.txtchess.api.ParameterDetector;
import org.cts.chess.txtchess.api.ParameterHandlerServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;
import org.cts.chess.txtchess.gae.db.DB_Util;
import org.cts.chess.txtchess.gae.db.Game;


@SuppressWarnings("serial")
public class ListGamesServlet extends ParameterHandlerServlet {

	@Override
	public ParameterDetector createParameterDetector() {
		return new ParameterDetector(new Parameter("List Type", null, "all",
				true, new Condition() {
					@Override
					public boolean validate(String input) {
						return ("all".equalsIgnoreCase(input)||TxtChessUtil.validateUserName(input)||"ai".equalsIgnoreCase(input));
					}
				}, "'all' or empty to see all your games, opponent username or ai to see the games with mentioned opponent"));
	}

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, final String mobileHash,
			List<String> message) throws Exception {
		final EntityManager manager = EMF.get().createEntityManager();
		String option=message.get(0);
		if(option==null)
			{option="";}
		try {
			ChessUser user = manager.find(ChessUser.class, mobileHash);		
			List<Game> games=DB_Util.getCurrentGames(manager, user.getMobileHash(),option);
			/*String startIndexString=request.getParameter("startIndex");
			ListIterator< Game> games= new ListIterator<Game>("Games currently Playing",games,startIndexString==null?1:Integer.parseInt(startIndexString),"/listGames?txtweb-message="+option+"&","") {

				@Override
				public String getRowLink(Game game) {
					boolean isWhite=mobileHash.equals(game.getWhite());
					String op=isWhite?game.getBlack():game.getWhite();
					ChessUser opponent = manager.find(ChessUser.class,op);
					String opponentName=TxtChessUtil.isAI_Game(game)?op:opponent.getUserName();
					return "<a href='./showGame?txtweb-message="+game.getId()+"'>Against "+ opponentName
							+ "( " +((isWhite==(game.getMovesCount()%2==0))?"Your":"Opponent") + " ["+((game.getMovesCount()%2==0)?"White":"Black" )+"] Turn )"
							+"</a>";
				}
			};
			
			request.setAttribute("__listIterator",games );
			setJspPath("/jsp/common/ListIteratorHandler.jsp");
			*/
			if(games.size()==1)
			{
				Game g=games.get(0);
				if(TxtChessUtil.isAI_Game(g))
				{
					option=TxtChessUtil.getAI_Player(g);
				}
				else if("all".equalsIgnoreCase(option))
				{
					ChessUser opponent=DB_Util.findUserByUserName(manager, mobileHash.equals(g.getWhite())?g.getBlack():g.getWhite());
					option=opponent.getUserName();
				}
				
				request.setAttribute("___message","You currently have only one game"+ " with "+option);
				setJspPath("/showGame?gameId="+g.getId());
				return;
			}
			
			request.setAttribute("__header","all".equalsIgnoreCase(option)?"All Current Games":"All Games against "+option);
			request.setAttribute("__games", games);
			setJspPath("/jsp/ListGames.jsp");
			} finally {
			manager.close();
		}
	}

	

	@Override
	public String getServiceEndPoint() {
		return "listGames";
	}

	@Override
	public String getServiceName() {
		return "@chezz.games";
	}

	@Override
	public String getExamples() {
		return "@chezz.games (list all your currrent games, same as @chezz.games all)<br/>"
		+"@chezz.games ai (list all your current games with computer)<br/>"
		+"@chezz.games wallace (list all your current games against wallace)<br/>";
	}

}
