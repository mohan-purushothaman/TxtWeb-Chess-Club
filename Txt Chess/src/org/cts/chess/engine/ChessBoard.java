package org.cts.chess.engine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import org.cts.chess.engine.Coin.CoinColor;
import org.cts.chess.engine.board.coins.King;
import org.cts.chess.engine.board.coins.Pawn;
import org.cts.chess.engine.board.coins.black.*;
import org.cts.chess.engine.board.coins.white.*;

/**
 * used for represent chessboard in data layer
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public abstract class ChessBoard {

	public static enum Status
	{
		PROGRESS("-"), 
		DRAW("1/2-1/2"),
		WHITE_WINS("1-0"),BLACK_WINS("0-1");
		private String result;

		private Status(String result) {
			this.result = result;
		}
		
		public String toString()
		{
			return result;
		}
	}
	public static enum Result
	{
		NO_RESULT(Status.PROGRESS,null),
		DRAW_AGREED(Status.DRAW,"Mutual Draw Agreement"), DRAW_STALEMATE(Status.DRAW,"Stalemate"), DRAW_METERIAL_INSUFFICIENT_FOR_WIN(Status.DRAW,"Meterial is Insufficient For Mate"),DRAW_FIFTY_MOVE_RULE(Status.DRAW,"FIFTY MOVE RULE"),DRAW_THREEFOLD_REPETITION(Status.DRAW,"THREEFOLD REPETITION"),
		WHITE_WINS_BY_CHECKMATE(Status.WHITE_WINS), BLACK_WINS_BY_CHECKMATE(Status.BLACK_WINS),
		WHITE_WINS_BY_RESIGNATION(Status.WHITE_WINS), BLACK_WINS_BY_RESIGNATION(Status.BLACK_WINS),
		WHITE_WINS_BY_TIMEOUT(Status.WHITE_WINS),BLACK_WINS_BY_TIMEOUT(Status.BLACK_WINS)
		;
		
		private Status status;
		private String infoString;
		private Result(Status status,String infoString) {
			this.status = status;
			this.infoString=infoString;
		}
		private Result(Status status)
		{
			this(status,null);
			infoString=name().replace('_', ' ');
		}
		
		public Status getStatus()
		{
			return status;
		}

		public String getInfoString() {
			return infoString;
		}
	}
    
    /**
     * used to store performed moves
     */
    private List<Move> moves = new LinkedList<Move>();
    /**
     * Represents WhiteKing in the board
     */
    private King white_king;
    /**
     * represents BlackKing in the board
     */
    private King black_king;
    
    
    /**
     * provide for initialisation purpose of board
     */
    public abstract void init();

    /**
     * to get coin at given position
     *
     * @param p position of coin
     * @return Coin at given position
     */
    public abstract Coin getCoinAt(Position p);

    /**
     * To set given Coin at specified position
     *
     * @param coin coin to be set
     * @param p position of coin
     */
    public abstract void setCoinAt(Coin coin, Position p);

    /**
     * add a new move to list of performwed moves not checking whether it is
     * valid move DO NOT CALL DIRECTLY IF YOU DON'T NEED just add this to list
     * of moves
     *
     * @param e move to be added
     */
    public final void addMove(Move e) {
        if (e != null) {
            moves.add(e);
            e.executeMove(this);
        }
    }

    /**
     * perform move after checking it's validness
     *
     * @param e move to be added
     * @return move is valid or not
     */
    public boolean performMove(Move e)throws Exception {
        if (e == null) {
            throw new Exception("Not an valid move");
        }
        
        if(getCurrentPlayerColor()!=e.getMovedCoin().getColor())
        {
        	throw new Exception(e.getInfo(this)+"is not a Valid Move for "+getCurrentPlayerColor().name());
        }
        
        List<Move> valid_moves = getCoinAt(e.getMovedCoinPosition()).getPossibleMoves(this);
        if (valid_moves != null) {
            Iterator<Move> it = valid_moves.iterator();
            while (it.hasNext()) {
                if (it.next().equals(e)) {
                    e.executeMove(this);
                    if (checkKingIsSafe(e.getMovedCoin().getColor())) {
                        e.undoMove(this);
                        addMove(e);
                        return true;
                    }
                    e.undoMove(this);
                    throw new Exception(e.getSAN(this)+" is a invalid move");
                }
            }
        }
        throw new Exception(e.getSAN(this)+" is a invalid move");
    }

    /**
     * remove last performed move and undo it
     */
    public synchronized void removeMove() {
        moves.remove(moves.size() - 1).undoMove(this);
    }

    /**
     * To get performed move of given index
     *
     * @param index position of move
     * @return Move at given index
     */
    public synchronized Move getMoveAt(int index) {
        return moves.get(index);
    }

    /**
     * count of performed moves
     *
     * @return count of moves
     */
    public synchronized int moveSize() {
        return moves.size();
    }

   
    
    @SuppressWarnings("deprecation")
	public boolean isHasThreat(Position p, CoinColor other_color) {

       for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
               Coin c=getCoinAt(Position.getPosition(j, i));
               if(c.getColor()==other_color)
               {
                   for (Move move : c.getMoves(this)) {
                       if(move.getEndPosition()==p)
                       {
                           return true;
                       }
                   }
               }
            }}
       return false;
        
//        return (checkForKnight(p, other_color)
//                || checkForPawn(p, other_color)
//                || checkForBishop_Queen(p, other_color)
//                || checkForRook_Queen(p, other_color));
    }

    /**
     * Set board coins to Standard Default chess positions must call after
     * init()
     */
    public final void setDefaultBoard() {
        for (int i = 0; i < 8; i++) {
            setCoinAt(new WhitePawn(), Position.getPosition(Position.TWO, i));
            setCoinAt(new BlackPawn(), Position.getPosition(Position.SEVEN, i));
        }
        setCoinAt(new WhiteKnight(), Position.getPosition(Position.ONE, Position.B));
        setCoinAt(new WhiteKnight(), Position.getPosition(Position.ONE, Position.G));
        setCoinAt(new BlackKnight(), Position.getPosition(Position.EIGHT, Position.B));
        setCoinAt(new BlackKnight(), Position.getPosition(Position.EIGHT, Position.G));

        setCoinAt(new WhiteBishop(), Position.getPosition(Position.ONE, Position.C));
        setCoinAt(new WhiteBishop(), Position.getPosition(Position.ONE, Position.F));
        setCoinAt(new BlackBishop(), Position.getPosition(Position.EIGHT, Position.C));
        setCoinAt(new BlackBishop(), Position.getPosition(Position.EIGHT, Position.F));

        setCoinAt(new WhiteRook(), Position.getPosition(Position.ONE, Position.A));
        setCoinAt(new WhiteRook(), Position.getPosition(Position.ONE, Position.H));
        setCoinAt(new BlackRook(), Position.getPosition(Position.EIGHT, Position.A));
        setCoinAt(new BlackRook(), Position.getPosition(Position.EIGHT, Position.H));

        setCoinAt(new WhiteQueen(), Position.getPosition(Position.ONE, Position.D));
        setCoinAt(new BlackQueen(), Position.getPosition(Position.EIGHT, Position.D));

        King w = new WhiteKing();
        King b = new BlackKing();
        setCoinAt(w, Position.getPosition(Position.ONE, Position.E));
        setCoinAt(b, Position.getPosition(Position.EIGHT, Position.E));

        setWhite_king(w);
        setBlack_king(b);
    }

    /**
     * To get color of current player
     *
     * @return
     */
    public CoinColor getCurrentPlayerColor() {
    	
    	return (getMoves().size()%2==0)?CoinColor.WHITE:CoinColor.BLACK;
    }
    
/*
    private boolean checkForBishop_Queen(Position p, CoinColor other_color) {
        return crossMove(p, other_color, -1, -1)
                || crossMove(p, other_color, -1, 1)
                || crossMove(p, other_color, 1, -1)
                || crossMove(p, other_color, 1, 1);
    }

    private boolean checkForKnight(Position p, CoinColor color) {
        Position tem;
        Coin c;
        for (int i = 0; i < 8; i++) {
            tem = Position.getPosition(p.getRank() + Knight.x[i], p.getFile() + Knight.y[i]);
            c = getCoinAt(tem);
            
              //c.getColor() != getColor()) 
             // it includes the condition c==Empty or opposite coin
              
             

            if (c != null && (c.getColor() == color) && c instanceof Knight) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForPawn(Position p, CoinColor color) {
        int temp = (color == CoinColor.WHITE) ? 1 : -1;

        Position tem = Position.getPosition(p.getRank() - temp, p.getFile() + 1);
        Coin c = getCoinAt(tem);
        if (c != null && c.getColor() == color && c instanceof Pawn) {
            return true;
        }
        tem = Position.getPosition(p.getRank() - temp, p.getFile() - 1);
        c = getCoinAt(tem);
        if (c != null && c.getColor() == color && c instanceof Pawn) {
            return true;
        }
        return false;
    }

    private boolean checkForRook_Queen(Position p, CoinColor other_color) {
        return strightMove(p, other_color, -1, 0)
                || strightMove(p, other_color, 1, 0)
                || strightMove(p, other_color, 0, -1)
                || strightMove(p, other_color, 0, 1);
    }
    
    */

    /**
     * Check whether given color King is in safe or not
     *
     * @param color color of King
     * @return if king don't have check returns true, false otherwise
     */
    public boolean checkKingIsSafe(CoinColor color) {
        if (color == CoinColor.WHITE) {
            return (!isHasThreat(white_king.getPosition(), CoinColor.BLACK));
        }
        return (!isHasThreat(black_king.getPosition(), CoinColor.WHITE));
    }

    /*
    private boolean crossMove(Position p, CoinColor color, int temp1, int temp2) {
        int i = 0, j = 0;
        Position temp;
        Coin c;
        while (true) {
            i = i + temp1;
            j = j + temp2;
            temp = Position.getPosition(p.getRank() - i, p.getFile() - j);
            c = getCoinAt(temp);
            if (c != null) {
                if (c != Coin.Empty) {
                    if (c.getColor() == color && (c instanceof Bishop || c instanceof Queen)) {
                        return true;
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        return false;
    }

    private boolean strightMove(Position p, CoinColor color, int temp1, int temp2) {
        int i = 0, j = 0;
        Position temp;
        Coin c;
        while (true) {
            i = i + temp1;
            j = j + temp2;
            temp = Position.getPosition(p.getRank() - i, p.getFile() - j);
            c = getCoinAt(temp);
            if (c != null) {
                if (c != Coin.Empty) {
                    if (c.getColor() == color && (c instanceof Rook || c instanceof Queen)) {
                        return true;
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        return false;
    }
*/
    /**
     * To get BlackKing in the board
     *
     * @return BlackKing of that board
     */
    public King getBlack_king() {
        return black_king;
    }

    public void setBlack_king(King black_king) {
        this.black_king = black_king;
    }

    /**
     * To get WhiteKing in the board
     *
     * @return WhiteKing of that board
     */
    public King getWhite_king() {
        return white_king;
    }

    public void setWhite_king(King white_king) {
        this.white_king = white_king;
    }

    /**
     * To get performed moves
     *
     * @return List of performed moves
     */
    public List<Move> getMoves() {
        return moves;
    }

    /**
     * To get coins in the board
     *
     * @return Iterator for coins in the board
     */
    public Iterator<Coin> getCoinIterator() {
        return new Iterator<Coin>() {
            private int rank = 0, file = 0;

            @Override
            public boolean hasNext() {
                return (rank < 8);
            }

            @Override
            public Coin next() {
                Coin temp = getCoinAt(Position.getPosition(rank, file));
                file++;
                if (file == 8) {
                    rank++;
                    file = 0;
                }
                return temp;
            }

            @Override
            public void remove() {
            }
        };
    }


    public Result getResult()
    {
    	Result result=checkForResult(getCurrentPlayerColor()==CoinColor.WHITE?CoinColor.BLACK:CoinColor.WHITE);
    	
    	return result==null?Result.NO_RESULT:result;
    }
    
	private Result checkForResult(CoinColor moveDoneBy) {
		List<Move> m=(moveDoneBy==CoinColor.WHITE?black_king:white_king).getPossibleMoves(this);	
		if(m.isEmpty())
		{
			Iterator<Coin> coinIterator=getCoinIterator();
			boolean hasNoLegalMoves=true;
			while(coinIterator.hasNext()){
				Coin c=coinIterator.next();
				if(c!=null&&c!=Coin.Empty&&c.getColor()!=moveDoneBy&&!c.getPossibleMoves(this).isEmpty())
				{
					hasNoLegalMoves=false;
					break;
				}
			}
			if(checkKingIsSafe(moveDoneBy==CoinColor.WHITE?CoinColor.BLACK:CoinColor.WHITE)){
				if(hasNoLegalMoves) return Result.DRAW_STALEMATE;
			}
			else if(hasNoLegalMoves){
			return moveDoneBy==CoinColor.WHITE?Result.WHITE_WINS_BY_CHECKMATE:Result.BLACK_WINS_BY_CHECKMATE;
			}
		}
		return null;
	}

	public String getFEN() {
		StringBuilder sb=new StringBuilder();
		for(int rank=7;rank>-1;rank--)
		{
			int count=0;
			for(int file=0;file<8;file++)
			{
				Coin c=getCoinAt(Position.getPosition(rank, file));
				if(c==null||c==Coin.Empty)
				{
					count++;
				}
				else
				{
					if(count>0)
					{
						sb.append(count);
						count=0;
					}
					sb.append(c.getColor()==CoinColor.WHITE?Character.toUpperCase(c.getNotation()):Character.toLowerCase(c.getNotation()));
				}
			}
			if(count>0)
			{
				sb.append(count);
				count=0;
			}
			sb.append('/');
		}
		sb.setCharAt(sb.length()-1, ' ');
		sb.append(getCurrentPlayerColor()==CoinColor.WHITE?'w':'b').append(' ');
		sb.append(getCastlingFEN_Segment()).append(' ');
		sb.append(getEnpassentSquare()).append(' ');
		sb.append(getFiftyMoveDrawCount()).append(' ');
		sb.append(1+((int)(getMoves().size()/2)));
		return sb.toString();
	}
	
	
	private String getFiftyMoveDrawCount() {
		int count=0;
		for(int i=getMoves().size()-1;i>-1;i--)
		{
			Move move=getMoveAt(i);
			if(!(move.getMovedCoin() instanceof Pawn)&&(move.getCapturedCoin()==null||move.getCapturedCoin()==Coin.Empty))
			{
				count++;
			}
			else
			{
				return String.valueOf(count);
			}
		}
		return String.valueOf(getMoves().size());
	}

	private String getCastlingFEN_Segment() {
		StringBuilder sb=new StringBuilder();
		if(getWhite_king().isNotMovedYet())
		{
			sb.append('K').append('Q');
		}
		if(getBlack_king().isNotMovedYet())
		{
			sb.append('k').append('q');
		}
		if(sb.length()==0)
		{
			sb.append("-");
		}
		return sb.toString();
	}
	
	public String getEnpassentSquare()
	{
		int movesCount=getMoves().size();
		if(movesCount!=0){
			Move m=getMoveAt(movesCount-1);
			if(m.getMovedCoin() instanceof Pawn){
				Position start=m.getStartPosition();
				if(Math.abs(start.getRank()-m.getEndPosition().getRank())==2)
					{
						return Position.getPosition(start.getRank()+(m.getMovedCoin().getColor()==CoinColor.WHITE?1:-1), start.getFile()).toString();
					}
				}
		}
		return "-";
	}

	public static ChessBoard getNewInstance()
	{
		return new ArrayChessBoard();
	}

	
	private static class ArrayChessBoard extends ChessBoard
	{

	    private Coin[][] board;

	    public ArrayChessBoard()
	    {
	        super();
	        init();
	        setDefaultBoard();
	        addMove(null);
	    }
	   

	    private Coin getCoinAt(int rank, int file)
	    {
	        return board[rank][file];
	    }

	    private void setCoinAt(Coin coin, int rank, int file)
	    {
	        board[rank][file] = coin;
	    }

	    @Override
	    public Coin getCoinAt(Position p)
	    {
	        if (p != null)
	        {
	            return getCoinAt(p.getRank(), p.getFile());
	        }
	        return null;
	    }

	    @Override
	    public void setCoinAt(Coin coin, Position p)
	    {
	        if (p != null)
	        {
	            coin.setPosition(p);
	            setCoinAt(coin, p.getRank(), p.getFile());
	        }
	    }

	    @Override
	    public void init()
	    {
	        board = new Coin[8][8];
	        for (int i = 0; i < 8; i++)
	        {
	            for (int j = 0; j < 8; j++)
	            {
	                board[i][j] = Coin.Empty;
	            }
	        }
	    }
	}	
}
