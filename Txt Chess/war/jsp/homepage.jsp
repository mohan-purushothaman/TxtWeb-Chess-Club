<%@include file="common/header.jsp" %>
<body>
	Welcome
	<%=user.getUserName()%>( <%= user.getRating() %>) <br/><br/>
	<%=request.getAttribute("__message")==null?"":request.getAttribute("__message")+"<br/><br/>" %>
	<a href="/createChallenge" >Create Challenge</a><br/>
	<a href="/listChallenges" >Challenges from Other Users</a><br/>
	<a href="/listChallenges?txtweb-message=all" >Open Challenges</a><br/>
	<a href="/listGames" >Current Games</a><br/>
	<a href="/listArchives">Game History</a><br/>
	<a href="/showBoards">Board Settings</a><br/>
</body>
<%@include file="common/footer.jspf" %>