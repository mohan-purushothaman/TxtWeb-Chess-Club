package org.cts.chess.txtchess.servlets;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.AbstractTxtChessBuilder;
import org.cts.chess.txtchess.api.TxtChessBuilderFactory;
import org.cts.chess.txtchess.api.TxtWebServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;




@SuppressWarnings("serial")
public class DefaultBoardServlet extends TxtWebServlet {

	
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message) throws Exception {
		EntityManager manager = EMF.get().createEntityManager();
		try{
		ChessUser user=manager.find(ChessUser.class,mobileHash );
		AbstractTxtChessBuilder builder=TxtChessBuilderFactory.getTxtBuilder(message);
		if(builder!=null)
		{
			EntityTransaction transaction = manager.getTransaction();
			try {
				transaction.begin();
			user.setBoardView(builder);
			manager.merge(user);
			transaction.commit();
			request.setAttribute("__message", "Your default board is now "+builder.getName());
			setJspPath("/jsp/common/MessageHandler.jsp");
			} catch (Exception e) {
				try {
					transaction.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				throw new Exception("Unable to set Board Settings (Internal Error)",e);
			}
		}
		}
		finally{
			manager.close();
		}
	}
	
}
