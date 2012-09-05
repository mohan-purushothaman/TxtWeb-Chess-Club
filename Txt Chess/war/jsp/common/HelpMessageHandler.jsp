<%@page import="org.cts.chess.txtchess.api.Parameter"%>
<%@page import="org.cts.chess.txtchess.api.ParameterDetector"%>
<%@include file="header.jsp" %>
<body>

<%
String serviceName=(String)request.getAttribute("__serviceName");
%>
<%="@chezz".equals(serviceName)&&user==null?"You have to register at @chezz to access all other services<br/>":"" %>
Possible Parameters for the service <%=serviceName==null?"":serviceName %>
<br/>
<%
ParameterDetector help=(ParameterDetector)request.getAttribute("__help");
if(help!=null)
{
%>
<%=help.getDescription() %><br/><br/>
<%
for(Parameter param:help.getParams())
{
%>
[<%=param.getName() %>] <%=param.isOptional()?"(optional)":"(must)" %>  -  <%=param.getHelpText() %><br/><br/>
<%
}
}
else
{
%>
No Parameters
<%
}
%>
<%
String examples=(String)request.getAttribute("__example");
if(examples!=null){
%>
Ex.<br/>
<%=examples %>
<%
}
 %>
</body>
<%@include file="footer.jspf" %>