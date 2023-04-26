package org.nschmidt.tictactoe;

import java.util.Arrays;
import java.util.Objects;

public record Board(boolean[] X, boolean[] O, char currentPlayer, int[] moves) {
    
    public static Board create() {
        return new Board(new boolean[9], new boolean[9], 'X', new int[] {0,1,2,3,4,5,6,7,8});
    }
    
    public Board applyMove(int move) {
        int[] nextMoves = Arrays.stream(moves).filter(m -> m != move).toArray();
        if (this.currentPlayer == 'X') {
            return new Board(applyMove(X, move), O, 'O', nextMoves);
        } else {
            return new Board(X, applyMove(O, move), 'X', nextMoves);
        }
    }
    
    public boolean isGameOver() {
        return isDraw() || wins('X') || wins('O');
    }
    
    public boolean isDraw() {
        return moves.length == 0;
    }
    
    public boolean wins(char player) {
        if (this.currentPlayer != player) {
            if (player == 'X') {
                return wins(X);
            }
            
            return wins(O);
        }
        
        return false;
    }
    
    private boolean[] applyMove(boolean[] state, int move) {
        boolean[] newState = new boolean[9];
        System.arraycopy(state, 0, newState, 0, 9);
        newState[move] = true;
        return newState;
    }
    
    private boolean wins(boolean[] state) {
        if (state[0] && state[1] && state[2]) return true;
        if (state[3] && state[4] && state[5]) return true;
        if (state[6] && state[7] && state[8]) return true;
        
        if (state[0] && state[3] && state[6]) return true;
        if (state[1] && state[4] && state[7]) return true;
        if (state[2] && state[5] && state[8]) return true;
        
        if (state[0] && state[4] && state[8]) return true;
        return state[2] && state[4] && state[6];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) sb.append('\n');
            if (X[i]) {
                sb.append('X');
            } else if (O[i]) {
                sb.append('O');
            } else {
                sb.append('-');
            }
        }
        
        sb.append('\n');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(O);
        result = prime * result + Arrays.hashCode(X);
        result = prime * result + Arrays.hashCode(moves);
        result = prime * result + Objects.hash(currentPlayer);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Board))
            return false;
        Board other = (Board) obj;
        return Arrays.equals(O, other.O) && Arrays.equals(X, other.X) && currentPlayer == other.currentPlayer
                && Arrays.equals(moves, other.moves);
    }
}
