package org.cts.chess.txtchess.servlets;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.ParameterDetector;
import org.cts.chess.txtchess.api.ParameterHandlerServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;
import org.cts.chess.txtchess.gae.db.DB_Util;

@SuppressWarnings("serial")
public class CreateUserServlet extends ParameterHandlerServlet {

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash,
			List<String> message) throws Exception {
		EntityManager manager = EMF.get().createEntityManager();
		ChessUser user = manager.find(ChessUser.class, mobileHash);
		try {
			if (user == null) {
				if (DB_Util.findUserByUserName(manager, message.get(0).toLowerCase()) == null) {
					setJspPath("/jsp/homepage.jsp");
					EntityTransaction transaction=manager.getTransaction();
					try{
					transaction.begin();
					manager.persist(new ChessUser(mobileHash, message.get(0)
							.toLowerCase(), 1200,request.getParameter("txtweb-protocol")));
					transaction.commit();
					}
					catch(Exception e)
					{
						try{
							transaction.rollback();
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
						throw new Exception("Unable to create new user (Internal Error), Please try again",e);
					}
				} else {
					setJspPath("/jsp/common/InputHandler.jsp");
					request.setAttribute("__endPoint", getServiceEndPoint());
					request.setAttribute("__paramName", "User Name");
					request.setAttribute(
							"__message",
							"given username is already been used by other user<br/> Please provide new UserName<br/>"+getParameterDetector().getParams().get(0).getHelpText());
				}
			}
			
		} finally {
			manager.close();
		}
	}

	@Override
	public ParameterDetector createParameterDetector() {
		return ParameterDetector.USERNAME_DETECTOR;
	}

	@Override
	public String getServiceEndPoint() {
		return "createUser";
	}

	@Override
	public String getServiceName() {
		return "@chezz";
	}

	@Override
	public boolean authenticated(String mobileHash, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return true;
	}

	@Override
	public String getExamples() {
		return "@chezz (username) (register you in the app with given user name)";
	}
	
	
}
