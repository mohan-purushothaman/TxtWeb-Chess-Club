<%@page import="org.cts.chess.txtchess.api.TxtWebApiUtil"%>
<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="org.cts.chess.txtchess.gae.db.ChessUser"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="org.cts.chess.txtchess.gae.db.DB_Util"%>
<%@page import="org.cts.chess.txtchess.gae.EMF"%>
<%@page import="org.cts.chess.txtchess.gae.db.ChessUser" %>
<!DOCTYPE html>
<html>
<head>
<title>Chezz on txtweb</title>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta name="txtweb-appkey" content="<%=TxtWebApiUtil.APP_KEY%>" />
</head>
<%
		EntityManager manager = EMF.get().createEntityManager();
		ChessUser user;
		String mobileHash=(String)request.getAttribute("txtweb-mobile");
		if(mobileHash==null)
		{
			%>
mobile number parameter not present in request
			<%
			return;
		}
		user = manager.find(ChessUser.class, mobileHash);
		
	%>