<%@include file="header.jsp" %>
<body>
<%=request.getAttribute("__message")==null?"":request.getAttribute("__message") %>
<br />
<form action='./<%=request.getAttribute("__endPoint") %>' class='txtweb-form' method="get">

<input type="hidden" name="<%=request.getAttribute("__oldParamName")==null?"__old":request.getAttribute("__oldParamName") %>" value="<%=request.getAttribute("__old")==null?"":request.getAttribute("__old") %>" />
<%=request.getAttribute("__paramName") %> <input type="text" name="txtweb-message" />
<input type='submit' value='submit' />
</form>
<br/>
<%=request.getAttribute("__footer")==null?"":request.getAttribute("__footer") %>
</body>
<%@include file="footer.jspf" %>