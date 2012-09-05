<%@include file="header.jsp" %>
<body>
<%=request.getAttribute("__message")==null?"":request.getAttribute("__message") %>
<br />
<%=request.getAttribute("__footer")==null?"":request.getAttribute("__footer") %>
</body>
<%@include file="footer.jspf" %>