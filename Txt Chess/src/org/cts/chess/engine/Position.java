/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cts.chess.engine;

/**
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public final class Position {

    private static final Position[][] position;
    private final int rank, file;
    public static final int A = 0, B = 1, C = 2, D = 3, E = 4, F = 5, G = 6, H = 7;
    public static final int ONE = A, TWO = B, THREE = C, FOUR = D, FIVE = E, SIX = F, SEVEN = G, EIGHT = H;
    public static final char[] f = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    public static final char[] r = {'1', '2', '3', '4', '5', '6', '7', '8'};

    private Position(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    @Override
    public String toString() {
        return getFileChar()+""+getRankChar();
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public static Position getPosition(int rank, int file) {
        if (rank > 7 || file > 7 || rank < 0 || file < 0) {
            return null;
        }
        return position[rank][file];
    }

    public char getFileChar() {
        return f[file];
    }

    public char getRankChar() {
        return r[rank];
    }

    static Position getSAN_EndPosition(String san) throws Exception {
        int file = -1, rank = -1;
        char c;
        for (int i = san.length() - 1; i > -1; i--) {
            if (rank == -1) {
                c = san.charAt(i);
                if (c >= '1' && c <= '8') {
                    rank = c - '1';
                }
            } else {
                c = san.charAt(i);
                if (c >= 'a' && c <= 'h') {
                    file = c - 'a';
                    return getPosition(rank, file);
                }
            }
        }
        throw new Exception("Not a valid Standard Arithmetic Notation");
    }
    
    static {
        position = new Position[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                position[i][j] = new Position(i, j);
            }
        }
    }
   
}
