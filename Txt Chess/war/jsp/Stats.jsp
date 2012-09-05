<%@include file="common/header.jsp" %>
<body>
<%
long total=user.getWon()+user.getDrawn()+user.getLost();
%>
	UserName - <%=user.getUserName() %><br/>
	Rating - <%=user.getRating() %><br/>
	Total Games - <%=total %><br/>
	<%
	if(total>0){
	%>
	Won - <%=user.getWon() +"( "+((long)(user.getWon()*100/total)) +"% )" %><br/>
	Lost - <%=user.getLost() +"( "+((long)(user.getLost()*100/total)) +"% )" %><br/>
	Drawn - <%=user.getDrawn() +"( "+((long)(user.getDrawn()*100/total)) +"% )" %><br/>
	<%
	}
	 %>
</body>
<%@include file="common/footer.jspf" %>