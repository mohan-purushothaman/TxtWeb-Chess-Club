
<%@page import="org.cts.chess.engine.ChessBoard"%>
<%@page import="org.cts.chess.txtchess.api.TxtChessBuilderFactory"%>
<%@page import="org.cts.chess.txtchess.api.AbstractTxtChessBuilder"%>
<%@include file="common/header.jsp" %>
<body>
<%
String className=request.getParameter("board"); 
AbstractTxtChessBuilder builder=TxtChessBuilderFactory.getTxtBuilder(className);
%>
<%= builder.getDescription()%><br/>
<%= builder.getChessBoard(ChessBoard.getNewInstance()) %>
<br/>
<%
if(builder.getClass().getName().equals(user.getBoardView())){
%>
Currently it is your Default Board
<%
}else{
%>
<br/>
<a href="/setDeaultBoard?txtweb-message=<%=builder.getClass().getName()%>">Set As Default Board</a>
<%
}
%>
</body>
<%@include file="common/footer.jspf" %>