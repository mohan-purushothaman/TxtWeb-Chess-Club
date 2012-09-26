package org.cts.chess.txtchess.gae.db;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Move;
import org.cts.chess.engine.ChessBoard.Result;
/**
 * Entity class for table GamesArchive
 * 
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class GamesArchive {
	@PrimaryKey	
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String FEN;

	@Persistent
	private String white;

	@Persistent
	private String black;

	@Persistent
	private int movesCount;

	@Persistent
	private String movesList;

	@Persistent
	private String drawRequestedBy;
	
	@Persistent
	private String result;
	

	public GamesArchive(Game game,Result result)
	{
		this(game.getFEN(),game.getWhite(),game.getBlack(),game.getMovesCount(),game.getMovesList(),game.getDrawRequestedBy(),result.name());
	}
	
	private GamesArchive(String FEN, String white, String black,
			int movesCount, String movesList, String drawRequestedBy,String result) {
		super();
		this.FEN = FEN;
		this.white = white;
		this.black = black;
		this.movesCount = movesCount;
		this.movesList = movesList;
		this.drawRequestedBy = drawRequestedBy;
		this.result=result;
	}

	public Long getId() {
		return id;
	}

	public String getFEN() {
		return FEN;
	}

	public String getWhite() {
		return white;
	}

	public String getBlack() {
		return black;
	}

	public int getMovesCount() {
		return movesCount;
	}

	public String getMovesList() {
		return movesList;
	}

	public String getDrawRequestedBy() {
		return drawRequestedBy;
	}

	public Result getResult() {
		return Result.valueOf(result);
	}
	
	public ChessBoard reCreateBoard() throws Exception
	{
		ChessBoard board=ChessBoard.getNewInstance();
		String moveList=getMovesList();
		if(moveList==null||moveList.isEmpty())
		{
			return board;
		}
		for(String move:moveList.split(String.valueOf(DB_Util.DELIMITER)))
		{
			board.addMove(Move.getMove(board, move));
		}
		return board;
	}
}
