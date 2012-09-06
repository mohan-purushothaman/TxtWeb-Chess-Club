package org.cts.chess.txtchess.gae.db;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import org.cts.chess.txtchess.api.AbstractTxtChessBuilder;
import org.cts.chess.txtchess.api.txtboards.SimpleTxtChessBoard2;
import org.cts.chess.txtchess.api.txtboards.UnicodeImageChessBoard;
import org.cts.chess.txtchess.gae.db.DB_Util.Protocol;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ChessUser {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String mobileHash;

	@Unique
	@Index
	@Persistent
	private String userName;

	@Persistent
	private int rating;

	@Persistent
	private String boardView;

	@Persistent
	private long won;

	@Persistent
	private long lost;

	@Persistent
	private long drawn;

	@Persistent
	private String protocol;

	public ChessUser(String mobileHash, String userName, int rating,
			String protocol) {
		super();
		this.mobileHash = mobileHash;
		this.userName = userName.toLowerCase();
		this.rating = rating;
		this.protocol = protocol;
		Protocol p = Protocol.getProtocol(protocol);

		boardView = UnicodeImageChessBoard.class.getName();
		/* making unicode chessboard as standard
		if(p==null)
			return;
		switch (p) {
		case EMULATOR: {

		}
		case INSTANT_MESSAGER: {
			boardView = UnicodeImageChessBoard.class.getName();
			break;
		}
		case SMS: {
			boardView = SimpleTxtChessBoard2.class.getName();
			break;
		}
		case USSD: {
			boardView = SimpleTxtChessBoard2.class.getName();
			break;
		}
		case WEB: {
			boardView = UnicodeImageChessBoard.class.getName();
			break;
		}
		}
		*/
	}

	public String getMobileHash() {
		return mobileHash;
	}

	public void setMobileHash(String mobileHash) {
		this.mobileHash = mobileHash;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getBoardView() {
		return boardView;
	}

	public void setBoardView(AbstractTxtChessBuilder boardBuilder) {
		this.boardView = boardBuilder.getClass().getName();
	}

	public long getWon() {
		return won;
	}

	public void won() {
		won++;
	}

	public long getLost() {
		return lost;
	}

	public void lost() {
		lost++;
	}

	public long getDrawn() {
		return drawn;
	}

	public void drawn() {
		drawn++;
	}

	public String getProtocol() {
		return protocol;
	}

}
