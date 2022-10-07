package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.BORDER_INDICIES;
import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;

public enum WinningChecker {
    INSTANCE;
    
    public static boolean wins(long state, long player) {
        final long opponent = player % 2 + 1;
        
        for (int i : BORDER_INDICIES) {
            final long borderPiece = lookAtField(state, i);
            final int[] neighbourIndicies = adjacency(i);
            if (check(state, player, opponent, borderPiece, neighbourIndicies, 0, 1)) return true;
            if (check(state, player, opponent, borderPiece, neighbourIndicies, 1, 0)) return true;
            if (check(state, player, opponent, borderPiece, neighbourIndicies, 2, 3)) return true;
            if (check(state, player, opponent, borderPiece, neighbourIndicies, 3, 2)) return true;
            if (check(state, player, opponent, borderPiece, neighbourIndicies, 4, 5)) return true;
            if (check(state, player, opponent, borderPiece, neighbourIndicies, 5, 4)) return true;
        }
        
        return false;
    }

    private static boolean check(long state, long player, long opponent, long borderPiece, int[] neighbourIndicies, int corner1, int corner2) {
        int[] nextNeighbourIndicies;
        return borderPiece == opponent 
                && neighbourIndicies[corner1] == -1 
                && neighbourIndicies[corner2] != -1 && player == lookAtField(state, neighbourIndicies[corner2])
                && (nextNeighbourIndicies = adjacency(neighbourIndicies[corner2]))[corner2] != -1 && player == lookAtField(state, nextNeighbourIndicies[corner2]);
    }
}