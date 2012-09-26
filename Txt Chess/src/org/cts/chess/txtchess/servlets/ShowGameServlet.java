package org.cts.chess.txtchess.servlets;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.ChessBoard.Result;
import org.cts.chess.engine.Move;
import org.cts.chess.engine.ChessBoard.Status;
import org.cts.chess.txtchess.api.TxtWebServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;
import org.cts.chess.txtchess.gae.db.Game;
import org.cts.chess.txtchess.gae.db.GamesArchive;

import com.valil.chesswebservice.Service;

@SuppressWarnings("serial")
public class ShowGameServlet extends TxtWebServlet {

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message)
			throws Exception {

		EntityManager manager = EMF.get().createEntityManager();
		try {
			ChessUser user = manager.find(ChessUser.class, mobileHash);
			String gameId = request.getParameter("gameId");
			if (gameId == null) {
				gameId = request.getAttribute("gameId").toString();
			}
			Game game = manager.find(Game.class, Long.parseLong(gameId));
			if (game == null) {
				throw new Exception("Game doesn't exist");
			}
			String info = (String) request.getAttribute("___message");
			try {
					boolean canContinue=performAI_Move(manager, request, game, mobileHash);
					if(!canContinue)
						return;
			} catch (Exception e) {
				e.printStackTrace();
				info = "AI is not able to perform the move now,Please try again later";
			}
			String drawMessage = "draw";
			String drawString = "Offer Draw";
			String rejectDrawSring="";
			if (info == null && game.getDrawRequestedBy() != null) {
				if (!game.getDrawRequestedBy().equals(mobileHash)) {
					info = "Your Opponent Offered you a Draw, Respond to your draw offer before making a move";
					drawString = "Accept Draw";
					//drawMessage = "cancel";
					rejectDrawSring="<br/><a href='/moveServlet?gameId="
							+ game.getId() + "&txtweb-message=cancel'>Reject Draw Offer</a>";
				} else {
					info = "Your opponent yet to respond to your Draw offer";
				}
			}
			info = (info == null ? "" : "<br/>" + info + "<br/><br/>");
			if (game.getMovesCount() == 0) {
				if (game.getWhite().equals(mobileHash)) {
					if (!TxtChessUtil.isAI_Game(game)) {
						request.setAttribute("__message", info+"You accepted "
								+ manager
										.find(ChessUser.class, game.getBlack())
										.getUserName() + " Challenge" 
								+ TxtChessUtil.getTxtChess(game.reCreateBoard(), user)
								+ "It's Your Turn, Reply with your Move<br/>"
								);
						request.setAttribute(
								"__footer",
								"<a href='/listMoves?gameId="
										+ game.getId()
										+ "'>Select Next Move from list</a><br/> <a href='/moveServlet?gameId="
										+ game.getId() + "&txtweb-message="
										+ drawMessage + "'>" + drawString
										+ "</a>" +rejectDrawSring+
										"<br/><a href='/moveServlet?gameId="
										+ game.getId()
										+ "&txtweb-message=resign'>Resign or Abort</a>");
					} else {
						request.setAttribute(
								"__message",
								"Please perform your first Move"
										+ TxtChessUtil.getTxtChess(ChessBoard
												.getNewInstance(),
												user));
						request.setAttribute(
								"__footer",
								"<a href='/listMoves?gameId="
										+ game.getId()
										+ "'>Select Next Move from list</a><br/> <a href='/moveServlet?gameId="
										+ game.getId() + "&txtweb-message="
										+ drawMessage + "'>" + drawString
										+ "</a>" +rejectDrawSring+
										"<br/><a href='/moveServlet?gameId="
										+ game.getId()
										+ "&txtweb-message=resign'>Resign or Abort</a>");
					}
					request.setAttribute("__paramName", "Your Move");
					request.setAttribute("__oldParamName", "gameId");
					request.setAttribute("__old", String.valueOf(game.getId()));
					request.setAttribute("__endPoint", "moveServlet");
				} else {
					request.setAttribute(
							"__message",info+"Your opponent haven't done any move, You will be notified when white performs a move"
									);
				}
			} else {
				ChessBoard chessBoard = game.reCreateBoard();
				Move lastMove = chessBoard.getMoveAt(game.getMovesCount() - 1);
				String header = info+ (game.getCurrentPlayer().equals(mobileHash) ? "Your Opponent performed "
						: "You made a move ")
						+ lastMove.getInfo(chessBoard);
				String board = TxtChessUtil.getTxtChess(game.reCreateBoard(), user);
				String footer = !game.getCurrentPlayer().equals(mobileHash) ? ("You will be notified when white performs a move")
						: ("It's Your Turn, Reply with your Move<br/>");
				request.setAttribute("__message", header + "<br/>" + board
						+ "<br/>" + footer);
				request.setAttribute("__paramName", "Your Move");
				request.setAttribute("__oldParamName", "gameId");
				request.setAttribute("__old", String.valueOf(game.getId()));
				request.setAttribute("__endPoint", "moveServlet");
				request.setAttribute(
						"__footer",
						"<a href='/listMoves?gameId="
								+ game.getId()
								+ "'>Select Next Move from list</a><br/> <a href='/moveServlet?gameId="
								+ game.getId() + "&txtweb-message="
								+ drawMessage + "'>" + drawString
								+ "</a>" +rejectDrawSring+
								"<br/><a href='/moveServlet?gameId="
								+ game.getId()
								+ "&txtweb-message=resign'>Resign or Abort</a>");
			}
			setJspPath(game.getCurrentPlayer().equals(mobileHash) ? "/jsp/common/InputHandler.jsp"
					: "/jsp/common/MessageHandler.jsp");
		} catch (Exception e) {
			throw new Exception("Unable to recreate the Board", e);
		} finally {
			manager.close();
		}

	}

	private boolean performAI_Move(EntityManager manager,
			HttpServletRequest request, Game game, String mobileHash)
			throws Exception {
		if (TxtChessUtil.isAI_Game(game) && TxtChessUtil.isAI_Move(game)) {
			ChessBoard board = game.reCreateBoard();
			String move = Service
					.getService()
					.getServiceSoap()
					.getNextMove(board.getFEN(), "",
							TxtChessUtil.getAI_Level(game));
			board.performMove(Move.getMove(board, move));
			game.init(board);
			Result result = board.getResult();
			if (result.getStatus() == Status.PROGRESS) {
				EntityTransaction transaction=manager.getTransaction();
				try{
				transaction.begin();
				manager.merge(game);
				transaction.commit();
				}
				catch (Exception e) {
					try {
						transaction.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					throw new Exception("Unable to accept challenge (Internal Error)",e);
				}
				return true;
			} else {
				GamesArchive g = new GamesArchive(game, result);
				EntityTransaction transaction=manager.getTransaction();
				try
				{
				manager.persist(g);
				transaction.begin();
				manager.remove(game);
				transaction.commit();
				}
				catch (Exception e) {
					try {
						transaction.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					throw new Exception("Unable to perform AI move (Internal Error)",e);
				}
				request.setAttribute("__message", TxtChessUtil.getHeaderStatus(
						result,
						game.getWhite(),
						game.getBlack(),
						mobileHash,
						"AI Level "
								+ TxtChessUtil.getAI_Level(game))
								+ "<br/><br/>"
								+ TxtChessUtil.getTxtChess(game.reCreateBoard(),
										manager.find(ChessUser.class,
												mobileHash)) + "<br/>");
				setJspPath("/jsp/common/MessageHandler.jsp");
				return false;
			}
		}
		return true;
	}

}
