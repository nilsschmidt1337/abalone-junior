package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;

public enum Attacker {
    INSTANCE;
    
    public static long[] performAttack(long state, long player) {
        final long opponent = player % 2 + 1;
        final long[] tempResult = new long[54];
        int attackCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int d = 0; d < 6; d++) {
                    attackCount += tryAttack(state, player, opponent, from, d, attackCount, neighbourIndicies, tempResult);
                }
            }
        }
        
        final long[] result = new long[attackCount];
        System.arraycopy(tempResult, 0, result, 0, attackCount);
        return result;
    }
    
    private static int tryAttack(long state, long player, long opponent, int from, int dir, int moveCount, int[] neighbourIndicies, long[] tempResult) {
        final int secondMarbleIndex = neighbourIndicies[dir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final long secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndicies = adjacency(secondMarbleIndex);
        final int targetForSecondMarbleIndex = secondMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (targetForSecondMarbleIndex == -1) return 0;
        final long targetForSecondMarble = lookAtField(state, targetForSecondMarbleIndex);
        // Das Feld hat keinen Gegner
        if (targetForSecondMarble != opponent) return 0;
        
        final int[] opponentNeighbourIndicies = adjacency(targetForSecondMarbleIndex);
        final int emptyPlaceForOpponentMarbleIndex = opponentNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das der Gegner gezogen werden kann:
        if (emptyPlaceForOpponentMarbleIndex == -1) return 0;
        final long emptyPlaceForOpponentMarble = lookAtField(state, emptyPlaceForOpponentMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForOpponentMarble != 0) return 0;
        
        state = move(state, opponent, targetForSecondMarbleIndex, emptyPlaceForOpponentMarbleIndex);
        state = move(state, player, secondMarbleIndex, targetForSecondMarbleIndex);
        state = move(state, player, from, secondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
