<%@page import="org.cts.chess.txtchess.gae.db.GamesArchive"%>
<%@page import="org.cts.chess.txtchess.servlets.TxtChessUtil"%>
<%@page import="org.cts.chess.txtchess.servlets.MoveServlet"%>
<%@page import="org.cts.chess.txtchess.gae.db.Game"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@include file="common/header.jsp"%>
<body>
<%=request.getAttribute("__header")%> <br/><br/>
	<%
	List<GamesArchive> list = new LinkedList<GamesArchive>();
	List<?> l = (List<?>) request.getAttribute("__games");
	for (Object temp : l) {
		list.add((GamesArchive) temp);
	}
%>
		<%
		int count=0;
			for (GamesArchive game : list) {
				boolean isWhite=user.getMobileHash().equals(game.getWhite());
				count++;
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
				String op=isWhite?game.getBlack():game.getWhite();
				ChessUser opponent = manager.find(ChessUser.class,op);
				String opponentName=TxtChessUtil.isAI_Game(game)?op:opponent.getUserName();
		%>
		<a href="./showArchive?gameId=<%=game.getId()%>">
		<%=result %> Against <%=opponentName%> in <%=game.getMovesCount() %> Moves (<%=game.getResult().getInfoString() %>)
		</a>
		<br/> 
		<br/>
		<%
 	}
 %>
	<%=count==0?"No Game History Available":""%>
</body>
<%@include file="common/footer.jspf"%>
