package org.cts.chess.txtchess.servlets;

import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.ChessBoard.Result;
import org.cts.chess.engine.ChessBoard.Status;
import org.cts.chess.engine.Coin.CoinColor;
import org.cts.chess.engine.Move;
import org.cts.chess.txtchess.api.TxtChessBuilderFactory;
import org.cts.chess.txtchess.api.TxtWebApiUtil;
import org.cts.chess.txtchess.api.txtboards.SimpleTxtChessBoard;
import org.cts.chess.txtchess.gae.db.ChessUser;
import org.cts.chess.txtchess.gae.db.Game;
import org.cts.chess.txtchess.gae.db.GamesArchive;

/**
 * Util class handling creation of alert messages
 * 
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */
public class TxtChessUtil {
	public static boolean pushMessage(EntityManager manager,
			String destinationMobileHash,String currentPlayer, Game game) {
		if(currentPlayer.equals(destinationMobileHash))
		{
			return false;
		}
		try {

			String header = "";
			String board = header;
			String footer = header;
			String form_link="/moveServlet";
			switch (game.getMovesCount()) {
			case 0: {
				if (game.getBlack().equals(destinationMobileHash)) {
					header = "Your Challenge accepted by "
							+ manager.find(ChessUser.class, game.getBlack())
									.getUserName()
							+ " , You will be notified when white performs a move";
				} else {
					header = "Your Challenge accepted by "
							+ manager.find(ChessUser.class, game.getWhite())
									.getUserName();
					board = getTxtChess(game.reCreateBoard(),manager.find(ChessUser.class, destinationMobileHash));
					footer = "It's Your Turn, Reply with your Move<br/><form action='"+form_link+"'><input type='hidden' name='gameId' value='"+game.getId()+"' /> Your Move <input type='text' name='txtweb-message'/><input type='submit' value='submit'/></form>";
				}
				break;
			}
			default: {
				ChessBoard chessBoard = game.reCreateBoard();
				Move lastMove = chessBoard.getMoveAt(game.getMovesCount() - 1);

				String drawMessage="draw";
				String drawString="Offer Draw";
				
				if(game.getDrawRequestedBy()!=null&&!destinationMobileHash.equals(game.getDrawRequestedBy()))
				{
					drawMessage="cancel";
					drawString="Accept Draw";
				}
				
				header = (game.getCurrentPlayer().equals(destinationMobileHash) ? "Your Opponent performed "
						: "You made a move ")
						+ lastMove.getName()
						+ " ( "
						+ lastMove.getSAN(chessBoard) + " )";
				board = getTxtChess(game.reCreateBoard(),manager.find(ChessUser.class, destinationMobileHash));
				footer = chessBoard.getCurrentPlayerColor() == CoinColor.WHITE ? ("You will be notified when white performs a move")
						: ("It's Your Turn, Reply with your Move<br/><form action='"+form_link+"'><input type='hidden' name='gameId' value='"+game.getId()+"' /> Your Move <input type='text' name='txtweb-message'/><input type='submit' value='submit'/></form>");
				footer=footer+"<br/>"+"<a href='/moveServlet?gameId="+game.getId()+"&txtweb-message="+drawMessage+"'>"+drawString+"</a><br/><a href='/moveServlet?gameId="+game.getId()+"&txtweb-message=resign'>Resign or Abort</a>";
				break;
			}
			}
			TxtWebApiUtil.pushMessage(destinationMobileHash, createMoveNotificationMessage(header , board , footer));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public static String createMoveNotificationMessage(String headMessage,
			String board,String footerMessage) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head><meta name='txtweb-appkey' content='")
				.append(TxtWebApiUtil.APP_KEY).append("' /></head>");
		sb.append("<body>").append(headMessage).append("<br/><br/>");
		sb.append(board);
		sb.append(footerMessage);
		sb.append("</body></html>");
		return sb.toString();
		
	}
	
	public static String getHeaderStatus(Result result, String white, String black,
			String currentPlayer, String opponentUsername) throws Exception {
		switch (result.getStatus()) {
		case DRAW: {
			return "Game Drawn with " + opponentUsername + "<br/> Reason - "
					+ result.getInfoString();
		}
		case PROGRESS: {
			throw new Exception("Unexpected error happened");
		}
		default: {
			return "You "
					+ ((result.getStatus() == Status.WHITE_WINS ? white : black)
							.equals(currentPlayer) ? "Won" : "Lost")
					+ " Against " + opponentUsername + "<br/> Reason - "
					+ result.getInfoString();
		}

		}
	}
	
	public static String getTxtChess(ChessBoard board,ChessUser user)
	{
		String boardView=user.getBoardView();
	return TxtChessBuilderFactory.getTxtBuilder(boardView==null?SimpleTxtChessBoard.class.getName():boardView).getChessBoard(board);
	}
	
	public static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{6,15}$");
	public static boolean validateUserName(String username)
	{
		return USERNAME_PATTERN.matcher(username).matches();
	}
	
	public static boolean isAI_Game(GamesArchive game) {
		return game.getWhite().startsWith("ai-")||game.getBlack().startsWith("ai-");
	}
	
	public static boolean isAI_Game(Game game) {
		return game.getWhite().startsWith("ai-")||game.getBlack().startsWith("ai-");
	}

	public static int getAI_Level(Game game) {
		return Integer.parseInt(getAI_Player(game).substring(3));
	}


	public static String getAI_Player(Game game)
	{
		return game.getWhite().startsWith("ai-")? game.getWhite():(game.getBlack().startsWith("ai-")?game.getBlack():null);
		
	}

	public static boolean isAI_Move(Game game) {
		return game.getCurrentPlayer().equals(getAI_Player(game));
	}

}
