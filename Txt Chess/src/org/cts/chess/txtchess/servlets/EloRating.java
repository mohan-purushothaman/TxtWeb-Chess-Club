package org.cts.chess.txtchess.servlets;

import org.cts.chess.engine.ChessBoard.Result;
import org.cts.chess.engine.ChessBoard.Status;
import org.cts.chess.txtchess.gae.db.ChessUser;

public class EloRating {
  
    public static final int K = 32;
    
   private static double expectancy(int whiteRating, int blackRating) {
        int diff = whiteRating - blackRating;
        return 1.0 / (1.0 + Math.pow(10.0,diff/400.0));
    }

    
    public static void adjust(ChessUser white,ChessUser black, Result result) {
        double ex = expectancy(white.getRating(),black.getRating());
        int delta = 0;
        if (result.getStatus()==Status.WHITE_WINS) {
            delta = (int) (K * ex);
            white.setRating(white.getRating() + delta);
            black.setRating(black.getRating() -delta);
        } else if(result.getStatus()==Status.BLACK_WINS){
            delta = (int) (K * (1.0 - ex));
            white.setRating(white.getRating() - delta);
            black.setRating(black.getRating() +delta);
            }
        else if(result.getStatus()==Status.DRAW)
        {
        	white.setRating(white.getRating()+1);
        	black.setRating(black.getRating()+2);
        }
    }

}