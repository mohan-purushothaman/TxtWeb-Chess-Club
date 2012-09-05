
<%@page import="org.cts.chess.txtchess.api.TxtChessBuilderFactory"%>
<%@page import="org.cts.chess.txtchess.api.AbstractTxtChessBuilder"%>
<%@page import="java.util.List" %>
<%@include file="common/header.jsp" %>
<body>
<%
List<AbstractTxtChessBuilder> list=TxtChessBuilderFactory.getAllInstances();
for(AbstractTxtChessBuilder builder:list){
	boolean defaultBoard=false;
	if(builder.equals(TxtChessBuilderFactory.getTxtBuilder(user.getBoardView())))
			{
		defaultBoard=true;
			}
%>
<a href="./showBoard?board=<%= builder.getClass().getName()%>" ><%=builder.getName()+(defaultBoard?" ( Current Board )":"") %></a><br/><br/>
<%
}
%>

</body>
<%@include file="common/footer.jspf" %>