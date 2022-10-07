package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;

public enum DoubleMover {
    INSTANCE;
    
    public static long[] moveTwoMarbles(long state, long player) {
        final long[] tempResult = new long[54];
        int moveCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int d = 0; d < 6; d++) {
                    moveCount += tryMove(state, player, from, d, 1, moveCount, neighbourIndicies, tempResult);
                    moveCount += tryMove(state, player, from, d, 3, moveCount, neighbourIndicies, tempResult);
                    moveCount += tryMove(state, player, from, d, 5, moveCount, neighbourIndicies, tempResult);
                }
            }
        }
        
        final long[] result = new long[moveCount];
        System.arraycopy(tempResult, 0, result, 0, moveCount);
        return result;
    }

    private static int tryMove(long state, long player, int from, int dir, int nextPieceDir, int moveCount, int[] neighbourIndicies, long[] tempResult) {

        final int emptyPlaceIndex = neighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das gezogen werden kann:
        if (emptyPlaceIndex == -1) return 0;
        final long emptyPlace = lookAtField(state, emptyPlaceIndex);
        // Das Feld ist nicht leer
        if (emptyPlace != 0) return 0;
        
        final int secondMarbleIndex = neighbourIndicies[nextPieceDir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final long secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndicies = adjacency(secondMarbleIndex);
        final int emptyPlaceForSecondMarbleIndex = secondMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (emptyPlaceForSecondMarbleIndex == -1) return 0;
        final long emptyPlaceForSecondMarble = lookAtField(state, emptyPlaceForSecondMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForSecondMarble != 0) return 0;
        
        state = move(state, player, from, emptyPlaceIndex);
        state = move(state, player, secondMarbleIndex, emptyPlaceForSecondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
