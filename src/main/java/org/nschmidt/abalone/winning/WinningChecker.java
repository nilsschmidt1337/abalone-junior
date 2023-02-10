package org.nschmidt.abalone.winning;

import static org.nschmidt.abalone.playfield.Adjacency.BORDER_INDICES;
import static org.nschmidt.abalone.playfield.Adjacency.adjacency;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT_FOR_WIN;
import static org.nschmidt.abalone.playfield.Field.lookAtField;
import static org.nschmidt.abalone.playfield.Player.EMPTY;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public enum WinningChecker {
    INSTANCE;
    
    public static boolean canWin(Field state, Player player) {
        if (PIECE_COUNT_FOR_WIN > 1) {
            Player opponent = player.switchPlayer();
            int lostPieces = PIECE_COUNT - Field.countPieces(state, opponent) + 1;
            if (lostPieces < PIECE_COUNT_FOR_WIN) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean wins(Field state, Player player) {
        if (!canWin(state, player)) {
            return false;
        }
        
        return gainPiece(state, player);
    }
    
    public static boolean gainPiece(Field state, Player player) {
        for (int i : BORDER_INDICES) {
            final Player borderPiece = lookAtField(state, i);
            if (borderPiece == EMPTY || borderPiece == player) continue;
            final int[] neighbourIndices = adjacency(i);
            if (check(state, player, neighbourIndices, 0, 1)) return true;
            if (check(state, player, neighbourIndices, 1, 0)) return true;
            if (check(state, player, neighbourIndices, 2, 3)) return true;
            if (check(state, player, neighbourIndices, 3, 2)) return true;
            if (check(state, player, neighbourIndices, 4, 5)) return true;
            if (check(state, player, neighbourIndices, 5, 4)) return true;
        }
        
        return false;
    }

    private static boolean check(Field state, Player player, int[] neighbourIndices, int corner1, int corner2) {
        int[] nextNeighbourIndices;
        final int neighbourIndicesCorner2 = neighbourIndices[corner2];
        return     neighbourIndicesCorner2 != -1
                && neighbourIndices[corner1] == -1
                && ((player == lookAtField(state, neighbourIndicesCorner2)
                && (nextNeighbourIndices = adjacency(neighbourIndicesCorner2))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndices[corner2]))
                || (player != lookAtField(state, neighbourIndicesCorner2)
                && EMPTY != lookAtField(state, neighbourIndicesCorner2)
                && (nextNeighbourIndices = adjacency(neighbourIndicesCorner2))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndices[corner2])
                && (nextNeighbourIndices = adjacency(nextNeighbourIndices[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndices[corner2])
                && (nextNeighbourIndices = adjacency(nextNeighbourIndices[corner2]))[corner2] != -1 
                && player == lookAtField(state, nextNeighbourIndices[corner2])));
    }
}
