package org.cts.chess.txtchess.gae.db;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.cts.chess.engine.ChessBoard;
import org.cts.chess.engine.Move;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Game {
	
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

public Game(ChessBoard board,String white, String black) {
	
	this.white = white;
	this.black = black;
	init(board);
}

public void init(ChessBoard board)
{
	this.FEN = board.getFEN();
	this.movesCount = board.getMoves().size();
	this.movesList = createMovesString(board,board.getMoves());
}

public String getFEN() {
	return FEN;
}

public void setFEN(String fEN) {
	FEN = fEN;
}

public String getWhite() {
	return white;
}

public void setWhite(String white) {
	this.white = white;
}

public String getBlack() {
	return black;
}

public void setBlack(String black) {
	this.black = black;
}

public int getMovesCount() {
	return movesCount;
}

public void setMovesCount(int movesCount) {
	this.movesCount = movesCount;
}

public String getMovesList() {
	return movesList;
}

public void setMovesList(String movesList) {
	this.movesList = movesList;
}

public Long getId() {
	return id;
}



public String getDrawRequestedBy() {
	return drawRequestedBy;
}

public void setDrawRequestedBy(String drawRequestedBy) {
	this.drawRequestedBy = drawRequestedBy;
}




private static String createMovesString(ChessBoard board,List<Move> moves) {
	StringBuilder sb=new StringBuilder(moves.size()*6);
	for(Move move:moves)
	{
		sb.append(move.getSAN(board)).append(DB_Util.DELIMITER);
	}
	if(sb.length()!=0)
	{
		sb.setLength(sb.length()-1);
	}
	return sb.toString();
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

public String getCurrentPlayer()
{
	return movesCount%2==0?getWhite():getBlack();
}
}
