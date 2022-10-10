package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;

public enum TripleToDoubleAttacker {
    INSTANCE;
    
    public static long[] performTripleToDoubleAttack(long state, long player) {
        final long opponent = player % 2 + 1;
        final long[] tempResult = new long[54];
        int attackCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int d = 0; d < 6; d++) {
                    attackCount += tryTripleToDoubleAttack(state, player, opponent, from, d, attackCount, neighbourIndicies, tempResult);
                }
            }
        }
        
        final long[] result = new long[attackCount];
        System.arraycopy(tempResult, 0, result, 0, attackCount);
        return result;
    }
    
    private static int tryTripleToDoubleAttack(long state, long player, long opponent, int from, int dir, int moveCount, int[] neighbourIndicies, long[] tempResult) {
        final int secondMarbleIndex = neighbourIndicies[dir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final long secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndicies = adjacency(secondMarbleIndex);
        final int thirdMarbleIndex = secondMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (thirdMarbleIndex == -1) return 0;
        final long targetForSecondMarble = lookAtField(state, thirdMarbleIndex);
        // Die dritte Murmel ist nicht vom selben Spieler
        if (targetForSecondMarble != player) return 0;
        
        final int[] thirdMarbleNeighbourIndicies = adjacency(thirdMarbleIndex);
        final int fourthMarbleIndex = thirdMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (fourthMarbleIndex == -1) return 0;
        final long targetForThirdMarble = lookAtField(state, fourthMarbleIndex);
        // Das Feld hat keinen Gegner
        if (targetForThirdMarble != opponent) return 0;
        
        final int[] fourthMarbleNeighbourIndicies = adjacency(fourthMarbleIndex);
        final int targetForFourthMarbleIndex = fourthMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die vierte Murmel gezogen werden kann:
        if (targetForFourthMarbleIndex == -1) return 0;
        final long targetForForthMarble = lookAtField(state, targetForFourthMarbleIndex);
        // Das Feld hat keinen Gegner
        if (targetForForthMarble != opponent) return 0;
        
        final int[] opponentNeighbourIndicies = adjacency(targetForFourthMarbleIndex);
        final int emptyPlaceForOpponentMarbleIndex = opponentNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das der Gegner gezogen werden kann:
        if (emptyPlaceForOpponentMarbleIndex == -1) return 0;
        final long emptyPlaceForOpponentMarble = lookAtField(state, emptyPlaceForOpponentMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForOpponentMarble != 0) return 0;
        
        state = move(state, opponent, targetForFourthMarbleIndex, emptyPlaceForOpponentMarbleIndex);
        state = move(state, opponent, fourthMarbleIndex, targetForFourthMarbleIndex);
        state = move(state, player, thirdMarbleIndex, fourthMarbleIndex);
        state = move(state, player, secondMarbleIndex, thirdMarbleIndex);
        state = move(state, player, from, secondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}