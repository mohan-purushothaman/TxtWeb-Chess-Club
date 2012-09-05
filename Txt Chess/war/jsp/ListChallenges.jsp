<%@page import="org.cts.chess.txtchess.gae.db.Challenge"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@include file="common/header.jsp"%>
<body>
	<%=request.getAttribute("__message") == null ? "" : request
					.getAttribute("__message") + "<br/><br/>"%>
	<%
		List<Challenge> list = new LinkedList<Challenge>();
		List<?> l = (List<?>) request.getAttribute("__challenges");
		for (Object temp : l) {
			list.add((Challenge) temp);
		}
	%>
	<%
		int count = 0;
		for (Challenge challenge : list) {
			if (challenge.getMin_rating() > user.getRating()
					|| challenge.getMax_rating() < user.getRating()
					|| challenge.getCreatedBy()
							.equals(user.getMobileHash())) {
				continue;
			}
			count++;
			ChessUser opponent = manager.find(ChessUser.class,
					challenge.getCreatedBy());
	%>
	<a href="./acceptChallenge?challengeId=<%=challenge.getId()%>"> <%="Play as " + challenge.getOpponentColor() + " vs "
						+ opponent.getUserName() + "(" + opponent.getRating()
						+ ")"%></a>
	<br />
	<%
		}
	%>
	<%=count != 0 ? "<br/><br/>Reply, '[option] reject' to reject the challenge"
					: ""%>
	<%=count == 0 ? "No Challenges Available" : ""%>
</body>
<%@include file="common/footer.jspf"%>
