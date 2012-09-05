package org.cts.chess.txtchess.servlets;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.TxtWebServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;




@SuppressWarnings("serial")
public class TxtChezzServlet extends TxtWebServlet {

	
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message) {
		EntityManager manager = EMF.get().createEntityManager();
		if(message!=null)
		{message=message.trim();}
		if("help".equalsIgnoreCase(message))
		{
			setJspPath("/createUser");
			return;
		}
		try{
		setJspPath(manager.find(ChessUser.class, mobileHash)==null? "/createUser":"/jsp/homepage.jsp");
		}
		finally{
			manager.close();
		}
	}
	
}
