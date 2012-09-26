package org.cts.chess.txtchess.servlets;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.Condition;
import org.cts.chess.txtchess.api.ListIterator;
import org.cts.chess.txtchess.api.Parameter;
import org.cts.chess.txtchess.api.ParameterDetector;
import org.cts.chess.txtchess.api.ParameterHandlerServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;
import org.cts.chess.txtchess.gae.db.DB_Util;
import org.cts.chess.txtchess.gae.db.GamesArchive;


@SuppressWarnings("serial")
public class ListArchivesServlet extends ParameterHandlerServlet {

	@Override
	public ParameterDetector createParameterDetector() {
		return new ParameterDetector(new Parameter("List Type", "all",
				true, new Condition() {
					@Override
					public boolean validate(String input) {
						return ("all".equalsIgnoreCase(input)||TxtChessUtil.validateUserName(input)||"ai".equalsIgnoreCase(input));
					}
				}, "'all' or empty to see all your archived games, opponent username or ai to see the archived games with mentioned opponent"));
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
			List<GamesArchive> games=DB_Util.getArchivedGames(manager, user.getMobileHash(),option);
			String startIndexString=request.getParameter("startIndex");
			ListIterator<GamesArchive> gamesIterator= new ListIterator<GamesArchive>("all".equalsIgnoreCase(option)?"Game History":"Game History Against "+option,games,startIndexString==null?1:Integer.parseInt(startIndexString),"/listArchives?txtweb-message="+option+"&","") {

				@Override
				public String getRowLink(GamesArchive game) {
					
					
					
					boolean isWhite=mobileHash.equals(game.getWhite());
					String op=isWhite?game.getBlack():game.getWhite();
					ChessUser opponent = manager.find(ChessUser.class,op);
					String opponentName=TxtChessUtil.isAI_Game(game)?op:opponent.getUserName();
					
				
					String result="Not Known";
					switch(game.getResult().getStatus())
					{
					case WHITE_WINS:
					{
						result=isWhite?"Won":"Lost";
						break;
					}
					case BLACK_WINS:
					{
						result=isWhite?"Lost":"Won";
						break;
					}
					case DRAW:
					{
						result="Drawn";
						break;
					}
					}
					
					
					return "<a href='/showArchive?gameId="+game.getId()+"'>" +
							result+" Against "+opponentName+" in "+game.getMovesCount() +" Moves ("+game.getResult().getInfoString() +")"
							+"</a>";
				}
			};
			
			request.setAttribute("__listIterator",gamesIterator );
			setJspPath("/jsp/common/ListIteratorHandler.jsp");
			
			} finally {
			manager.close();
		}
	}

	

	@Override
	public String getServiceEndPoint() {
		return "listArchives";
	}

	@Override
	public String getServiceName() {
		return "@chezz.history";
	}

	@Override
	public String getExamples() {
	return "@chezz.history (list all your finished games, same as @chezz.history all)<br/>"
	+"@chezz.history ai (list all your finished games with computer)<br/>"
	+"@chezz.history wallace (list all your finished games against wallace)";
	}

}
