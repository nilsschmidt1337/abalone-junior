package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.BORDER_INDICIES;
import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Player.EMPTY;

public enum WinningChecker {
    INSTANCE;
    
    public static boolean wins(Field state, Player player) {
        for (int i : BORDER_INDICIES) {
            final Player borderPiece = lookAtField(state, i);
            final int[] neighbourIndicies = adjacency(i);
            if (check(state, player, borderPiece, neighbourIndicies, 0, 1)) return true;
            if (check(state, player, borderPiece, neighbourIndicies, 1, 0)) return true;
            if (check(state, player, borderPiece, neighbourIndicies, 2, 3)) return true;
            if (check(state, player, borderPiece, neighbourIndicies, 3, 2)) return true;
            if (check(state, player, borderPiece, neighbourIndicies, 4, 5)) return true;
            if (check(state, player, borderPiece, neighbourIndicies, 5, 4)) return true;
        }
        
        return false;
    }

    private static boolean check(Field state, Player player, Player borderPiece, int[] neighbourIndicies, int corner1, int corner2) {
        int[] nextNeighbourIndicies;
        return borderPiece != player && borderPiece != EMPTY
                && neighbourIndicies[corner1] == -1 
                && neighbourIndicies[corner2] != -1
                && ((player == lookAtField(state, neighbourIndicies[corner2])
                && (nextNeighbourIndicies = adjacency(neighbourIndicies[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndicies[corner2]))
                || (player != lookAtField(state, neighbourIndicies[corner2])
                && EMPTY != lookAtField(state, neighbourIndicies[corner2])
                && (nextNeighbourIndicies = adjacency(neighbourIndicies[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndicies[corner2])
                && (nextNeighbourIndicies = adjacency(nextNeighbourIndicies[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndicies[corner2])
                && (nextNeighbourIndicies = adjacency(nextNeighbourIndicies[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndicies[corner2])));
    }
}
