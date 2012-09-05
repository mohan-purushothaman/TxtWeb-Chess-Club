package org.cts.chess.txtchess.servlets;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.TxtWebServlet;
import org.cts.chess.txtchess.gae.EMF;

@SuppressWarnings("serial")
public class ShowBoardsServlet extends TxtWebServlet {

	
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message) {
		EntityManager manager = EMF.get().createEntityManager();
		try{
		setJspPath("/jsp/ShowBoards.jsp");
		}
		finally{
			manager.close();
		}
	}
	
}
