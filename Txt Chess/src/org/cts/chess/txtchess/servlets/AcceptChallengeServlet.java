package org.cts.chess.txtchess.servlets;


import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.TxtWebServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;
import org.cts.chess.txtchess.gae.db.DB_Util;
import org.cts.chess.txtchess.gae.db.Game;

@SuppressWarnings("serial")
public class AcceptChallengeServlet extends TxtWebServlet {

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message)throws Exception {

		EntityManager manager = EMF.get().createEntityManager();
		try {
			Game game = DB_Util.acceptChallenge(manager,
					Long.parseLong(request.getParameter("challengeId")),
					mobileHash,"reject".equalsIgnoreCase(message));
			ChessUser user=manager.find(ChessUser.class, mobileHash);
			
			if (game.getWhite().equals(mobileHash)) {
					TxtChessUtil.pushMessage(manager, game.getBlack(),mobileHash, game);

					setJspPath("/jsp/common/InputHandler.jsp");

					request.setAttribute(
							"__message",
							"You accepted "
									+ manager.find(ChessUser.class,
											game.getBlack()).getUserName()
									+ " Challenge"
									+ TxtChessUtil.getTxtChess(game.reCreateBoard(),user)
											+ "It's Your Turn, Reply with your Move<br/>");
					request.setAttribute("__paramName", "Your Move");
					request.setAttribute("__oldParamName", "gameId");
					request.setAttribute("__old", String.valueOf(game.getId()));
					request.setAttribute("__endPoint", "moveServlet");
				
			} else {
				setJspPath("/jsp/common/MessageHandler.jsp");
				request.setAttribute(
						"__message",
						"Your game with "
								+ manager
										.find(ChessUser.class, game.getWhite())
										.getUserName()
								+ " is started, You will be notified when white performs a move");
				TxtChessUtil.pushMessage(manager, game.getWhite(),mobileHash, game);
			}
		} finally {
			manager.close();
		}

	}

}
