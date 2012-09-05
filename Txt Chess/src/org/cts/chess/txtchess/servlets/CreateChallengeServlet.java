package org.cts.chess.txtchess.servlets;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Move;
import org.cts.chess.txtchess.api.Condition;
import org.cts.chess.txtchess.api.Parameter;
import org.cts.chess.txtchess.api.ParameterDetector;
import org.cts.chess.txtchess.api.ParameterHandlerServlet;
import org.cts.chess.txtchess.api.TxtWebApiUtil;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.Challenge;
import org.cts.chess.txtchess.gae.db.DB_Util;
import org.cts.chess.txtchess.gae.db.ChessUser;
import org.cts.chess.txtchess.gae.db.Game;

import com.valil.chesswebservice.Service;

@SuppressWarnings("serial")
public class CreateChallengeServlet extends ParameterHandlerServlet {
	@Override
	public ParameterDetector createParameterDetector() {
		return new ParameterDetector(new Parameter("Opponent Name", null, null,
				false, new Condition() {

					@Override
					public boolean validate(String input) {
						return ("all".equalsIgnoreCase(input)
								|| "ai".equalsIgnoreCase(input) || TxtChessUtil
								.validateUserName(input));
					}
				}, "[all or AI or another username]"), new Parameter(
				"Your Coin Color", new String[] { "white", "black" }, "white",
				true, new Condition() {

					@Override
					public boolean validate(String input) {
						return input.equalsIgnoreCase("white")
								|| input.equalsIgnoreCase("black");
					}
				}, "[white or black]"), new Parameter(
				"Minimum Rating for Opponent or AI Difficult level(1 to 6)", null, "0", true,
				Condition.NUMBER, "[* or number or (1-6) if opponent is AI]"), new Parameter(
				"Maximum Rating for Opponent", null, "4000", true,
				Condition.NUMBER, "[* or number]"));
	}

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash,
			List<String> message) throws Exception {
		EntityManager manager = EMF.get().createEntityManager();
		try {
			String user = mobileHash; // manager.find(ChessUser.class,
										// mobileHash).getMobileHash();
			String opponent = getOpponent(manager, message.get(0));
			int color = message.get(1).equalsIgnoreCase("white") ? 1 : 0;
			if ("ai".equalsIgnoreCase(opponent)) {
				int level = Integer.parseInt(message.get(2));
				if (level > 6)
					{level = 6;}
				if (level <= 0) {
					level = 3;
				}
				ChessBoard board = ChessBoard.getNewInstance();
				Game game = new Game(board, color == 1 ? mobileHash : "ai-"
						+ level, color == 0 ? mobileHash : "ai-" + level);
				if (TxtChessUtil.isAI_Move(game)) {
					// manager.persist(game);
					String move = Service.getService().getServiceSoap()
							.getNextMove(board.getFEN(), "", level);
					board.performMove(Move.getMove(board, move));
					game.init(board);
				}
				EntityTransaction transaction = manager.getTransaction();
				try {
					transaction.begin();
					manager.persist(game);
					transaction.commit();
				} catch (Exception e) {
					try {
						transaction.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					throw new Exception(
							"Unable to create Game with Computer(Internal Error) , Please Try Again",e);
				}
				request.setAttribute("gameId", game.getId());
				setJspPath("/showGame");
				return;
			}
			
			
			if (user.equals(opponent)) {
				throw new Exception("You can't challenge yourself");
			}

			int minRating = (opponent != null) || message.get(2).equals("*") ? 0
					: Integer.parseInt(message.get(2));
			int maxRating = (opponent != null) || message.get(3).equals("*") ? 4000
					: Integer.parseInt(message.get(3));
			Challenge challenge = new Challenge(user, opponent, color,
					minRating, maxRating, new Date(Calendar.getInstance()
							.getTime().getTime()));
			EntityTransaction transaction = manager.getTransaction();
			try {
				transaction.begin();
				manager.persist(challenge);
				transaction.commit();
			} catch (Exception e) {
				try {
					transaction.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				throw new Exception(
						"Unable to create challenge (Internal Error)",e);
			}
			
			if(opponent!=null)
			{
				ChessUser currentUser=manager.find(ChessUser.class, user);
				TxtWebApiUtil.pushMessage(opponent, 
					TxtChessUtil.createMoveNotificationMessage("You have been challenged by "+currentUser.getUserName()+" ("+currentUser.getRating() +")",
							"Your color is "+challenge.getOpponentColor(), "<br/><br/><a href='./acceptChallenge?challengeId="+challenge.getId()+"'>Accept Challenge</a><br/>" +
									"<a href='./acceptChallenge?challengeId="+challenge.getId()+"&txtweb-message=reject'>Reject Challenge</a>"));
			}
			
			request.setAttribute("__message", "Challenge is created, You will be notified if challenge is accepted");
			setJspPath("/jsp/common/MessageHandler.jsp");
		}
		/*
		 * catch(Exception e) { setJspPath("/jsp/common/InputHandler.jsp");
		 * request.setAttribute("__endPoint", getServiceEndPoint());
		 * request.setAttribute("__paramName", "User Name");
		 * request.setAttribute( "__message", e.getMessage()+
		 * "<br/> Please provide other UserName or all for open challenge or ai to play with computer"
		 * );
		 * 
		 * }
		 */
		finally {
			manager.close();
		}
	}

	private String getOpponent(EntityManager manager, String username)
			throws Exception {
		if ("ai".equalsIgnoreCase(username)) {
			return username;
		}
		if (username.equalsIgnoreCase("all"))
			return null;
		ChessUser opponent = DB_Util.findUserByUserName(manager, username);
		if (opponent == null) {
			throw new Exception("No user exist with username '" + username
					+ "'");
		}
		return opponent.getMobileHash();
	}

	@Override
	public String getServiceEndPoint() {
		return "createChallenge";
	}

	@Override
	public String getServiceName() {
		return "@chezz.challenge";
	}

	@Override
	public String getExamples() {
		return "@chezz.challenge ai black 5 (starts a game with ai level 5 and you are playing black)<br/>"
		+"@chezz.challenge ai 6 (starts a game with ai level 6 and you are playing white)<br/>"
		+"@chezz.challenge all black 1200 1400 (open challenge users having rating between 1200 to 1400 and you are playing white)<br/>"
		+"@chezz.challenge wallace black (challenge 'wallace'(my username) for a game and you are playing black )";
	}

}
