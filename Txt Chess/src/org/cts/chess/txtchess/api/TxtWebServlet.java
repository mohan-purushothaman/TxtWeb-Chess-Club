package org.cts.chess.txtchess.api;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;

@SuppressWarnings("serial")
public abstract class TxtWebServlet extends HttpServlet {

	private String jspPath;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String mobileHash = request.getParameter("txtweb-mobile");
		
		 //if(mobileHash==null) { mobileHash="testingMobile"; }
		 

		String message = request.getParameter("txtweb-message");
		if (message == null)
			{message = "";}

		message = message.trim();

		try {
			if (!authenticated(mobileHash, request, response)) {
				return;
			}
			process(request, response, mobileHash, message);
			if (getJspPath() == null) {
				return;
			}
		} catch (Exception e) {
			setJspPath("/jsp/common/MessageHandler.jsp");
			request.setAttribute("__message", e.getMessage());
			e.printStackTrace();
		}
		try {
			request.setAttribute("txtweb-mobile", mobileHash);
			renderResponse(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	public abstract void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message)
			throws Exception;

	public String getJspPath() {
		return jspPath;
	}

	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}

	protected void renderResponse(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		getServletConfig().getServletContext()
				.getRequestDispatcher(getJspPath()).forward(request, response);
	}

	public boolean authenticated(String mobileHash, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		EntityManager manager = EMF.get().createEntityManager();
		try {
			if (mobileHash == null || mobileHash.isEmpty()) {
				setJspPath("/jsp/common/MessageHandler.jsp");
				renderResponse(request, response);
				return false;
			}
			ChessUser user = manager.find(ChessUser.class, mobileHash);

			if (user == null) {
				setJspPath("/createUser");
				renderResponse(request, response);
				return false;
			}
		} finally {
			manager.close();
		}
		return true;
	}
}
