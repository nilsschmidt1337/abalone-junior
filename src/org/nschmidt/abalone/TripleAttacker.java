package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;
import static org.nschmidt.abalone.Player.EMPTY;

public enum TripleAttacker {
    INSTANCE;
    
    public static long[] performTripleAttack(long state, Player player) {
        final Player opponent = player.switchPlayer();
        final long[] tempResult = new long[54];
        int attackCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int d = 0; d < 6; d++) {
                    attackCount += tryTripleAttack(state, player, opponent, from, d, attackCount, neighbourIndicies, tempResult);
                }
            }
        }
        
        final long[] result = new long[attackCount];
        System.arraycopy(tempResult, 0, result, 0, attackCount);
        return result;
    }
    
    private static int tryTripleAttack(long state, Player player, Player opponent, int from, int dir, int moveCount, int[] neighbourIndicies, long[] tempResult) {
        final int secondMarbleIndex = neighbourIndicies[dir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final Player secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndicies = adjacency(secondMarbleIndex);
        final int thirdMarbleIndex = secondMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (thirdMarbleIndex == -1) return 0;
        final Player targetForSecondMarble = lookAtField(state, thirdMarbleIndex);
        // Die dritte Murmel ist nicht vom selben Spieler
        if (targetForSecondMarble != player) return 0;
        
        final int[] thirdMarbleNeighbourIndicies = adjacency(thirdMarbleIndex);
        final int targetForThirdMarbleIndex = thirdMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (targetForThirdMarbleIndex == -1) return 0;
        final Player targetForThirdMarble = lookAtField(state, targetForThirdMarbleIndex);
        // Das Feld hat keinen Gegner
        if (targetForThirdMarble != opponent) return 0;
        
        final int[] opponentNeighbourIndicies = adjacency(targetForThirdMarbleIndex);
        final int emptyPlaceForOpponentMarbleIndex = opponentNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das der Gegner gezogen werden kann:
        if (emptyPlaceForOpponentMarbleIndex == -1) return 0;
        final Player emptyPlaceForOpponentMarble = lookAtField(state, emptyPlaceForOpponentMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForOpponentMarble != EMPTY) return 0;
        
        state = move(state, opponent, targetForThirdMarbleIndex, emptyPlaceForOpponentMarbleIndex);
        state = move(state, player, thirdMarbleIndex, targetForThirdMarbleIndex);
        state = move(state, player, secondMarbleIndex, thirdMarbleIndex);
        state = move(state, player, from, secondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
