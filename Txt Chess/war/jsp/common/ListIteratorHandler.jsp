<%@page import="org.cts.chess.txtchess.api.ListIterator"%>
<%@include file="header.jsp" %>
<body>
<%
ListIterator<?> listIterator=(ListIterator<?>)request.getAttribute("__listIterator");
%>

<%=listIterator.getHeader()%>
<br />
<%=listIterator.getListString() %> <br/>
<%=listIterator.getCallbackURLSegment() %>
</body>
<%@include file="footer.jspf" %>