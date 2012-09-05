package org.cts.chess.txtchess.servlets;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.ChessBoard.Result;
import org.cts.chess.engine.ChessBoard.Status;
import org.cts.chess.engine.Move;
import org.cts.chess.txtchess.api.TxtWebApiUtil;
import org.cts.chess.txtchess.api.TxtWebServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.ChessUser;
import org.cts.chess.txtchess.gae.db.DB_Util;
import org.cts.chess.txtchess.gae.db.Game;
import org.cts.chess.txtchess.gae.db.GamesArchive;

import com.valil.chesswebservice.Service;

@SuppressWarnings("serial")
public class MoveServlet extends TxtWebServlet {

	public enum Operation {
		MOVE, DRAW, DRAW_CANCEL, RESIGN;
	}

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message)
			throws Exception {

		EntityManager manager = EMF.get().createEntityManager();
		try {
			Game game = manager.find(Game.class,
					Long.parseLong(request.getParameter("gameId")));
			if ((!mobileHash.equals(game.getWhite()))
					&& (!mobileHash.equals(game.getBlack()))) {
				throw new Exception("You are not part of this Game");
			}
			Operation operation = getOperation(message);
			String opponent = game.getWhite().equals(mobileHash) ? game
					.getBlack() : game.getWhite();
			switch (operation) {
			case MOVE: {
				if (TxtChessUtil.isAI_Game(game)) {
					performAI_Move(manager, request, mobileHash, game, message);
				} else if (game.getDrawRequestedBy() == null
						|| mobileHash.equals(game.getDrawRequestedBy())) {
					performMove(manager,request, mobileHash, game, message);
				} else {
					setJspPath("/showGame");
				}
				break;
			}
			case DRAW: {
				if (TxtChessUtil.isAI_Game(game)) {
					request.setAttribute("___message", opponent
							+ " rejected your draw offer");
					setJspPath("/showGame");
				} else if (game.getDrawRequestedBy() == null
						|| game.getDrawRequestedBy().equals(mobileHash)) {
					game.setDrawRequestedBy(mobileHash);
					EntityTransaction transaction = manager.getTransaction();
					try {
						transaction.begin();
						manager.merge(game);
						transaction.commit();
					} catch (Exception e) {
						try {
							transaction.rollback();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						throw new Exception(
								"Unable to perform your draw request (Internal Error)",e);
					}
					TxtWebApiUtil
							.pushMessage(
									opponent,
									TxtChessUtil
											.createMoveNotificationMessage(
													"Your Opponent Offered you a Draw",
													TxtChessUtil.getTxtChess(
															game.reCreateBoard(),
															manager.find(
																	ChessUser.class,
																	opponent)),
													"<a href='./moveServlet?gameId="
															+ game.getId()
															+ "&txtweb-message=draw'>Accept Draw offer</a><br/><a href='./moveServlet?gameId="
															+ game.getId()
															+ "&txtweb-message=cancel'>Reject Draw offer</a><br/><a href='./moveServlet?gameId="
															+ game.getId()
															+ "&txtweb-message=resign'>Resign or Abort</a>"));
					request.setAttribute("___message",
							"Your Opponent notified of Draw offer");
					setJspPath("/showGame");
				} else {
					gamesEnded(manager, request, game, Result.DRAW_AGREED,
							mobileHash);
				}

				break;
			}
			case DRAW_CANCEL: {
				game.setDrawRequestedBy(null);
				EntityTransaction transaction = manager.getTransaction();
				try {
					transaction.begin();
					manager.merge(game);
					transaction.commit();
				} catch (Exception e) {
					try {
						transaction.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					throw new Exception(
							"Unable to perform your draw cancel request (Internal Error)",e);
				}
				if (!TxtChessUtil.isAI_Game(game)) {
					TxtWebApiUtil
							.pushMessage(
									opponent,
									TxtChessUtil
											.createMoveNotificationMessage(
													"Your Opponent Rejected Your Draw Offer",
													TxtChessUtil.getTxtChess(
															game.reCreateBoard(),
															manager.find(
																	ChessUser.class,
																	opponent)),
													"<a href='./moveServlet?gameId="
															+ game.getId()
															+ "&txtweb-message=draw'>Accept / Offer Draw</a><br/><a href='./moveServlet?gameId="
															+ game.getId()
															+ "&txtweb-message=resign'>Resign or Abort</a>"));
				}
				request.setAttribute("___message",
						"You have rejected your opponent draw offer");
				setJspPath("/showGame");
				break;
			}
			case RESIGN: {
				gamesEnded(manager, request, game, (mobileHash.equals(game
						.getWhite()) ? Result.BLACK_WINS_BY_RESIGNATION
						: Result.WHITE_WINS_BY_RESIGNATION), mobileHash);
				break;
			}
			}
		} finally {
			manager.close();
		}
	}

	private void performAI_Move(EntityManager manager,
			HttpServletRequest request, String mobileHash, Game game,
			String move) throws Exception {
		EntityTransaction transaction = manager.getTransaction();

		if (!game.getCurrentPlayer().equals(mobileHash)) {
			throw new Exception("It is not your turn to move, Please wait for "
					+ game.getCurrentPlayer() + " Move");
		}
		ChessBoard board = game.reCreateBoard();
		board.performMove(Move.getMove(board, move));
		game.init(board);
		Result result = board.getResult();
		int level = TxtChessUtil.getAI_Level(game);
		switch (result.getStatus()) {
		case PROGRESS: {
			String aiMove = Service.getService().getServiceSoap()
					.getNextMove(game.getFEN(), "", level);
			board.performMove(Move.getMove(board, aiMove));
			game.init(board);
			result = board.getResult();
			if (result.getStatus() == Status.PROGRESS) {
				try {
					transaction.begin();
					manager.merge(game);
					transaction.commit();
				} catch (Exception e) {
					try {
						transaction.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					throw new Exception(
							"Ai is unable to perform it's move (Internal Error)",e);
				}
				setJspPath("/showGame");
				break;
			} else {
				// ai move ended the game, just follow next default case
			}

		}
		default: {
			GamesArchive g = new GamesArchive(game, result);
			try {
				manager.persist(g);
				transaction.begin();
				manager.remove(game);
				transaction.commit();
			} catch (Exception e) {
				try {
					transaction.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				throw new Exception(
						"Unable to perform the AI move (Internal Error)",e);
			}
			request.setAttribute(
					"__message",
					TxtChessUtil.getHeaderStatus(result, game.getWhite(),
							game.getBlack(), mobileHash, "AI Level " + level)
							+ "<br/><br/>"
							+ TxtChessUtil.getTxtChess(game.reCreateBoard(),
									manager.find(ChessUser.class, mobileHash))
							+ "<br/>");
			setJspPath("/jsp/common/MessageHandler.jsp");
			break;
		}

		}
	}

	private void performMove(EntityManager manager,HttpServletRequest request,
			String mobileHash, Game game, String move) throws Exception {
		EntityTransaction transaction=manager.getTransaction();
		if (!game.getCurrentPlayer().equals(mobileHash)) {
			throw new Exception("It is not your turn to move, Please wait for "
					+ (game.getMovesCount() % 2 == 0 ? "White" : "Black")
					+ " Move");
		}
		ChessBoard board = game.reCreateBoard();
		board.performMove(Move.getMove(board, move));
		game.init(board);
		Result result = board.getResult();
		switch (result.getStatus()) {
		case PROGRESS: {
			if (!TxtChessUtil.isAI_Game(game)) {
				TxtChessUtil.pushMessage(manager, game.getCurrentPlayer(),
						mobileHash, game);
			}
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
				throw new Exception("Unable to perform your move (Internal Error)",e);
			}
			request.setAttribute("__message",
					"Your Opponent notified of your Move ");
			setJspPath("/jsp/common/MessageHandler.jsp");
			break;
		}
		default: {
			gamesEnded(manager, request, game, result, mobileHash);
			break;
		}
		}
		
	}

	private void gamesEnded(EntityManager manager, HttpServletRequest request,
			Game game, Result result, String currentUser) throws Exception {
		DB_Util.gameEnded(manager, game, result);
		String opponent = game.getWhite().equals(currentUser) ? game.getBlack(): game.getWhite();
		if (!TxtChessUtil.isAI_Game(game)) {
			TxtWebApiUtil.pushMessage(opponent, TxtChessUtil
					.createMoveNotificationMessage(TxtChessUtil
							.getHeaderStatus(result, game.getWhite(), game
									.getBlack(), game.getCurrentPlayer(),
									manager.find(ChessUser.class, opponent)
											.getUserName()), TxtChessUtil
							.getTxtChess(game.reCreateBoard(),
									manager.find(ChessUser.class, opponent)),
							""));
		}
		request.setAttribute(
				"__message",
				TxtChessUtil.getHeaderStatus(
						result,
						game.getWhite(),
						game.getBlack(),
						currentUser,
						opponent.equals(TxtChessUtil.getAI_Player(game)) ? opponent
								: manager.find(ChessUser.class, opponent)
										.getUserName())
						+ "<br/><br/>"
						+ TxtChessUtil.getTxtChess(game.reCreateBoard(),
								manager.find(ChessUser.class, currentUser))
						+ "<br/>");
		setJspPath("/jsp/common/MessageHandler.jsp");
	}

	private Operation getOperation(String message) {
		if ("resign".equalsIgnoreCase(message)) {
			return Operation.RESIGN;
		} else if ("draw".equalsIgnoreCase(message)) {
			return Operation.DRAW;
		} else if ("cancel".equalsIgnoreCase(message)) {
			return Operation.DRAW_CANCEL;
		}
		return Operation.MOVE;
	}

}
