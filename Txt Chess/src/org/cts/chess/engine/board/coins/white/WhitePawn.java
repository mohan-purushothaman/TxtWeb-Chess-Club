/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cts.chess.engine.board.coins.white;

import org.cts.chess.engine.Position;
import org.cts.chess.engine.board.coins.Pawn;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public class WhitePawn extends Pawn
{
    @Override
    public boolean isNotYetMoved() {
        return (position.getRank() == Position.TWO);
    }

    @Override
    public CoinColor getColor() {
        return CoinColor.WHITE;
    }

    @Override
    public boolean canEnpassent() {
        return getRank()==Position.FIVE;
    }

    @Override
    public boolean readyForPromote() {
        return getRank()==Position.SEVEN;
    }

	
}
