package org.cts.chess.txtchess.gae.db;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.ChessBoard.Result;
import org.cts.chess.txtchess.servlets.EloRating;
import org.cts.chess.txtchess.servlets.TxtChessUtil;

public class DB_Util {
	public static final char DELIMITER=',';
	public static enum Protocol {
		SMS("1000"), USSD("1001"), WEB("200"), EMULATOR("2100"), INSTANT_MESSAGER(
				"220");
		private String startsWith;

		Protocol(String startsWith) {
			this.startsWith = startsWith;
		}

		public static Protocol getProtocol(String protocol) {
			if (protocol == null)
				return null;
			for (Protocol p : values()) {
				if (protocol.startsWith(p.startsWith)) {
					return p;
				}
			}
			return null;
		}
	}

	public static void gameEnded(EntityManager manager, Game game, Result result)
			throws Exception {
		GamesArchive archivedGame = new GamesArchive(game, result);
		EntityTransaction transaction = manager.getTransaction();
		try {
			if (!TxtChessUtil.isAI_Game(game)) {
				ChessUser white = manager
						.find(ChessUser.class, game.getWhite());
				ChessUser black = manager
						.find(ChessUser.class, game.getBlack());
				EloRating.adjust(white, black, result);
				switch (result.getStatus()) {
				case DRAW: {
					white.drawn();
					black.drawn();
					break;
				}
				case BLACK_WINS: {
					black.won();
					white.lost();
					break;
				}

				case WHITE_WINS: {
					white.won();
					black.lost();
					break;
				}
				default: {
					throw new Exception("UNRECOGNIZED ERROR");
				}
				}
				manager.merge(white);
				manager.merge(black);
			}
			manager.persist(archivedGame);
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
					"Unable to Archive or end the game (Internal Error)", e);
		}

	}

	public static List<Game> getCurrentGames(EntityManager manager,
			String userMobileHash, String option) throws Exception {
		boolean isAi = "ai".equalsIgnoreCase(option);
		boolean listAll = "all".equalsIgnoreCase(option);
		boolean isUserName = TxtChessUtil.validateUserName(option);
		if (isUserName) {
			ChessUser op = findUserByUserName(manager, option);
			if (op == null)
				throw new Exception("No user exist with name " + option);
			option = op.getMobileHash();
		}
		List<Game> result = new LinkedList<Game>();
		Query query1 = manager.createQuery("select c from "
				+ Game.class.getName() + " c where c.white='" + userMobileHash
				+ "'");
		for (Object obj : query1.getResultList()) {
			if (obj instanceof Game) {
				Game game = (Game) obj;
				if (isAi && TxtChessUtil.isAI_Game(game)) {
					result.add(game);
				} else if (isUserName&&game.getBlack().equals(option)) {
					result.add(game);
				} else if(listAll){
					result.add(game);
				}
			}
		}

		Query query2 = manager.createQuery("select c from "
				+ Game.class.getName() + " c where c.black='" + userMobileHash
				+ "'");
		for (Object obj : query2.getResultList()) {
			if (obj instanceof Game) {
				Game game = (Game) obj;
				if (isAi && TxtChessUtil.isAI_Game(game)) {
					result.add(game);
				} else if (isUserName && game.getWhite().equals(option)) {
					result.add(game);
				} else if(listAll){
					result.add(game);
				}
			}
		}

		return result;
	}

	public static List<GamesArchive> getArchivedGames(EntityManager manager,
			String userMobileHash, String option) throws Exception {
		boolean isAi = "ai".equalsIgnoreCase(option);
		boolean listAll = "all".equalsIgnoreCase(option);
		boolean isUserName = TxtChessUtil.validateUserName(option);
		if (isUserName) {
			ChessUser op = findUserByUserName(manager, option);
			if (op == null)
				throw new Exception("No user exist with name " + option);
			option = op.getMobileHash();
		}
		List<GamesArchive> result = new LinkedList<GamesArchive>();
		Query query1 = manager.createQuery("select c from "
				+ GamesArchive.class.getName() + " c where c.white='" + userMobileHash
				+ "'");
		for (Object obj : query1.getResultList()) {
			if (obj instanceof GamesArchive) {
				GamesArchive game = (GamesArchive) obj;
				if (isAi && TxtChessUtil.isAI_Game(game)) {
					result.add(game);
				} else if (isUserName&&game.getBlack().equals(option)) {
					result.add(game);
				} else if(listAll){
					result.add(game);
				}
			}
		}

		Query query2 = manager.createQuery("select c from "
				+ GamesArchive.class.getName() + " c where c.black='" + userMobileHash
				+ "'");
		for (Object obj : query2.getResultList()) {
			if (obj instanceof Game) {
				GamesArchive game = (GamesArchive) obj;
				if (isAi && TxtChessUtil.isAI_Game(game)) {
					result.add(game);
				} else if (isUserName && game.getWhite().equals(option)) {
					result.add(game);
				} else if(listAll){
					result.add(game);
				}
			}
		}

		return result;
	}

	
	public static List<Challenge> getChallenges(EntityManager manager,
			String userMobileHash, int userRating, boolean listAll) {

		String condition = "= '" + userMobileHash + "'";
		if (listAll) {
			condition = " IS NULL";
		}
		Query query = manager.createQuery("select c from "
				+ Challenge.class.getName() + " c where c.max_rating >= "
				+ userRating + " AND c.opponent " + condition);
		List<Challenge> result = new LinkedList<Challenge>();
		for (Object obj : query.getResultList()) {
			if (obj instanceof Challenge) {
				Challenge challenge = (Challenge) obj;
				if (challenge.getMin_rating() <= userRating
						&& challenge.getMax_rating() >= userRating
						&& !challenge.getCreatedBy().equals(userMobileHash)) {
					result.add(challenge);
				}
			}
		}
		return result;
	}

	public static Game acceptChallenge(EntityManager manager, Long challengeId,
			String mobileHash, boolean reject) throws Exception {

		Challenge challenge = manager.find(Challenge.class, challengeId);
		if (challenge == null) {
			throw new Exception("Challenge already accepted by other user");
		}

		if (reject) {
			if (mobileHash.equals(challenge.getOpponent())) {
				EntityTransaction transaction = manager.getTransaction();
				try {
					transaction.begin();
					manager.remove(challenge);
					transaction.commit();
					throw new Exception(
							"You have rejected your opponent challenge");
				} catch (Exception e) {
					try {
						transaction.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					throw new Exception(
							"unable to reject the challenge (Internal error)");
				}
			} else {
				throw new Exception(
						"It's an open challenge, you can't cancel it");
			}
		}

		if (challenge.getCreatedBy().equals(mobileHash)) {
			throw new Exception("You can't accept challenges created by you");
		}
		ChessUser user = manager.find(ChessUser.class, mobileHash);
		if (challenge != null
				&& challenge.getMin_rating() <= user.getRating()
				&& challenge.getMax_rating() >= user.getRating()
				&& (challenge.getOpponent() == null || mobileHash
						.equals(challenge.getOpponent()))) {
			EntityTransaction transaction = manager.getTransaction();
			try {

				Game game = new Game(
						ChessBoard.getNewInstance(),
						"white".equals(challenge.getCreatorColor()) ? challenge
								.getCreatedBy() : user.getMobileHash(),
						!"white".equals(challenge.getCreatorColor()) ? challenge
								.getCreatedBy() : user.getMobileHash());
				manager.persist(game);
				transaction.begin();
				manager.remove(challenge);
				transaction.commit();
				return game;
			} catch (Exception e) {
				try {
					transaction.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				throw new Exception(
						"Unable to accept challenge (Internal Error)", e);
			}

		}
		throw new Exception("Unable to Accept the Challenge");
	}

	public static ChessUser findUserByUserName(EntityManager manager,
			String name) {

		return (ChessUser) findByUniqueColumn(manager,
				ChessUser.class.getName(), "userName", name.toLowerCase(), true);
	}

	private static Object findByUniqueColumn(EntityManager manager,
			String tableName, String columnName, String columnValue,
			boolean isStringType) {
		List<?> l = findByColumn(manager, tableName, columnName, columnValue,
				isStringType);
		return l.isEmpty() ? null : l.get(0);
	}

	private static List<?> findByColumn(EntityManager manager,
			String tableName, String columnName, String columnValue,
			boolean isStringType) {
		if (isStringType) {
			columnValue = "'" + columnValue + "'";
		}
		Query q = manager.createQuery("select c from " + tableName
				+ " c where c." + columnName + "=" + columnValue);
		return q.getResultList();
	}
}
