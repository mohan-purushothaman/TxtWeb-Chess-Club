<%@page import="org.cts.chess.engine.ChessBoard.Status"%>
<%@page import="org.cts.chess.txtchess.gae.db.GamesArchive"%>
<%@page import="org.cts.chess.txtchess.servlets.TxtChessUtil"%>
<%@page import="org.cts.chess.txtchess.servlets.MoveServlet"%>
<%@page import="org.cts.chess.txtchess.gae.db.Game"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@include file="common/header.jsp"%>
<body>

<%
GamesArchive archive=(GamesArchive)request.getAttribute("__archive");
boolean isWhite=archive.getWhite().equals(user.getMobileHash());
String opponent=isWhite?archive.getBlack():archive.getWhite();
%>
Opponent - <%= TxtChessUtil.isAI_Game(archive)?opponent:manager.find(ChessUser.class, opponent) %><br/><br/>
You Played as <%= isWhite?"White":"Black" %><br/><br/>
Game Result - <%=archive.getResult().getStatus().toString() %> ( <%=archive.getResult().getInfoString() %> )<br/><br/>

<%=TxtChessUtil.getTxtChess(archive.reCreateBoard(), user) %>

</body>
<%@include file="common/footer.jspf"%>
