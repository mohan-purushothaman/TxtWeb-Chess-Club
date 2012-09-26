package org.cts.chess.txtchess.api;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;

/**
 * A basic TxtWeb Servlet which can handle single word message . 
 * Basic class for all txtweb services with authentication, it will verify all requests with TxtWeb
 * 
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */

@SuppressWarnings("serial")
public abstract class TxtWebServlet extends HttpServlet {

	private String jspPath;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String mobileHash = request
				.getParameter(TxtWebApiUtil.TXTWEB_MOBILE_PARAM);

		// if(mobileHash==null) { mobileHash="testingMobile1"; }
		try {
				if (!TxtWebApiUtil.isAuthenticatedRequest(request)) {
					throw new Exception("Unauthorized Request");
					/*
					 * 403 error code would be exact for strict authorization
					 * response.sendError(403); return; 
					 */
				}
			
			String message = request
					.getParameter(TxtWebApiUtil.TXTWEB_MESSAGE_PARAM);
			if (message == null) {
				message = "";
			}

			message = message.trim();

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
			request.setAttribute(TxtWebApiUtil.TXTWEB_MOBILE_PARAM, mobileHash);
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
