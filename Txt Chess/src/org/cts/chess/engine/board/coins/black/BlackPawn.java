/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cts.chess.engine.board.coins.black;

import org.cts.chess.engine.Position;
import org.cts.chess.engine.board.coins.Pawn;
/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public class BlackPawn extends Pawn {

    @Override
    public boolean isNotYetMoved() {
        return (position.getRank() == Position.SEVEN);
    }

    @Override
    public CoinColor getColor() {
        return CoinColor.BLACK;
    }

    
    @Override
    public boolean canEnpassent() {
        return getRank() == Position.FOUR;
    }

    @Override
    public boolean readyForPromote() {
        return getRank() == Position.TWO;
    }

	}
