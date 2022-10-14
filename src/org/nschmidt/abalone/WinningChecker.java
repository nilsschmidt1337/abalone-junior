package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.BORDER_INDICES;
import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Player.EMPTY;

public enum WinningChecker {
    INSTANCE;
    
    public static boolean wins(Field state, Player player) {
        for (int i : BORDER_INDICES) {
            final Player borderPiece = lookAtField(state, i);
            final int[] neighbourIndices = adjacency(i);
            if (check(state, player, borderPiece, neighbourIndices, 0, 1)) return true;
            if (check(state, player, borderPiece, neighbourIndices, 1, 0)) return true;
            if (check(state, player, borderPiece, neighbourIndices, 2, 3)) return true;
            if (check(state, player, borderPiece, neighbourIndices, 3, 2)) return true;
            if (check(state, player, borderPiece, neighbourIndices, 4, 5)) return true;
            if (check(state, player, borderPiece, neighbourIndices, 5, 4)) return true;
        }
        
        return false;
    }

    private static boolean check(Field state, Player player, Player borderPiece, int[] neighbourIndices, int corner1, int corner2) {
        int[] nextNeighbourIndices;
        return borderPiece != player && borderPiece != EMPTY
                && neighbourIndices[corner1] == -1 
                && neighbourIndices[corner2] != -1
                && ((player == lookAtField(state, neighbourIndices[corner2])
                && (nextNeighbourIndices = adjacency(neighbourIndices[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndices[corner2]))
                || (player != lookAtField(state, neighbourIndices[corner2])
                && EMPTY != lookAtField(state, neighbourIndices[corner2])
                && (nextNeighbourIndices = adjacency(neighbourIndices[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndices[corner2])
                && (nextNeighbourIndices = adjacency(nextNeighbourIndices[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndices[corner2])
                && (nextNeighbourIndices = adjacency(nextNeighbourIndices[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndices[corner2])));
    }
}
