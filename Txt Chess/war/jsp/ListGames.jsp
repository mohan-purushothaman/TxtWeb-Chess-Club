<%@page import="org.cts.chess.txtchess.servlets.TxtChessUtil"%>
<%@page import="org.cts.chess.txtchess.servlets.MoveServlet"%>
<%@page import="org.cts.chess.txtchess.gae.db.Game"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@include file="common/header.jsp"%>
<body>
<%=request.getAttribute("__header")%> <br/><br/>
	<%
	List<Game> list = new LinkedList<Game>();
	List<?> l = (List<?>) request.getAttribute("__games");
	for (Object temp : l) {
		list.add((Game) temp);
	}
%>
		<%
		int count=0;
			for (Game game : list) {
				boolean isWhite=user.getMobileHash().equals(game.getWhite());
				count++;
				String op=isWhite?game.getBlack():game.getWhite();
				ChessUser opponent = manager.find(ChessUser.class,op);
				String opponentName=TxtChessUtil.isAI_Game(game)?op:opponent.getUserName();
		%>
		<a href="./showGame?gameId=<%=game.getId()%>">
		Against 
		<%=opponentName
		+ "( " +((isWhite==(game.getMovesCount()%2==0))?"Your":"Opponent") + " ["+((game.getMovesCount()%2==0)?"White":"Black" )+"] Turn )"
		%></a>
		<br/> 
		<br/>
		<%
 	}
 %>
	<%=count==0?"No Games Available":""%>
</body>
<%@include file="common/footer.jspf"%>
