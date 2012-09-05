package org.cts.chess.txtchess.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cts.chess.txtchess.api.TxtWebServlet;



@SuppressWarnings("serial")
public class ShowBoardServlet extends TxtWebServlet {

	
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message) {
		if(message!=null)
		{message=message.trim();}
		if("help".equalsIgnoreCase(message))
		{
			request.setAttribute("__help", null);
			request.setAttribute("__serviceName", "@chezz.boards");
			setJspPath("/jsp/common/HelpMessageHandler.jsp");
			return;
		}
		setJspPath("/jsp/ShowBoardInfo.jsp");
		
	}
	
}
