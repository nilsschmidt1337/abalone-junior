package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;
import static org.nschmidt.abalone.Player.EMPTY;

import java.math.BigInteger;

public enum DoubleMover {
    INSTANCE;
    
    public static BigInteger[] moveTwoMarbles(BigInteger state, Player player) {
        final BigInteger[] tempResult = new BigInteger[54];
        int moveCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int d = 0; d < 6; d++) {
                    for (int d2 = 0; d2 < 6; d2++) {
                        moveCount += tryMove(state, player, from, d, d2, moveCount, neighbourIndicies, tempResult);
                    }
                }
            }
        }
        
        final BigInteger[] result = new BigInteger[moveCount];
        System.arraycopy(tempResult, 0, result, 0, moveCount);
        return result;
    }

    private static int tryMove(BigInteger state, Player player, int from, int dir, int nextPieceDir, int moveCount, int[] neighbourIndicies, BigInteger[] tempResult) {

        final int emptyPlaceIndex = neighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das gezogen werden kann:
        if (emptyPlaceIndex == -1) return 0;
        final Player emptyPlace = lookAtField(state, emptyPlaceIndex);
        // Das Feld ist nicht leer
        if (emptyPlace != EMPTY) return 0;
        
        final int secondMarbleIndex = neighbourIndicies[nextPieceDir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final Player secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndicies = adjacency(secondMarbleIndex);
        final int emptyPlaceForSecondMarbleIndex = secondMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (emptyPlaceForSecondMarbleIndex == -1) return 0;
        final Player emptyPlaceForSecondMarble = lookAtField(state, emptyPlaceForSecondMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForSecondMarble != EMPTY && emptyPlaceForSecondMarbleIndex != from) return 0;
        
        state = move(state, player, from, emptyPlaceIndex);
        state = move(state, player, secondMarbleIndex, emptyPlaceForSecondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
