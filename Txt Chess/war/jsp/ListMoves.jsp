<%@page import="org.cts.chess.txtchess.servlets.TxtChessUtil"%>
<%@page import="org.cts.chess.txtchess.servlets.MoveServlet"%>
<%@page import="org.cts.chess.txtchess.gae.db.Game"%>
<%@page import="org.cts.chess.engine.*"%>

<%@page import="java.util.*"%>
<%@page import="java.util.LinkedList"%>
<%@include file="common/header.jsp"%>
<body>

<%

List<Move> allMoves=new LinkedList<Move>();
ChessBoard board=(ChessBoard)request.getAttribute("chessboard");
Iterator<Coin> it=board.getCoinIterator();
		while(it.hasNext())
		{
			Coin c=it.next();
			if(c!=null&c!=Coin.Empty&&c.getColor()==board.getCurrentPlayerColor())
			{
				allMoves.addAll(c.getPossibleMoves(board));
			}
		}
		
		%>
		<%
		for(Move m:allMoves)
		{
		%>
		<a href="./moveServlet?gameId=<%=request.getParameter("gameId")%>&txtweb-message=<%=m.getSAN(board)%>"><%= m.getInfo(board) %></a>
		<%
		}
		%>
		</body>
<%@include file="common/footer.jspf"%>