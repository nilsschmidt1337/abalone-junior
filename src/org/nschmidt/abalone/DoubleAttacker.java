package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;
import static org.nschmidt.abalone.Player.EMPTY;

import java.math.BigInteger;

public enum DoubleAttacker {
    INSTANCE;
    
    public static BigInteger[] performDoubleAttack(BigInteger state, Player player) {
        final Player opponent = player.switchPlayer();
        final BigInteger[] tempResult = new BigInteger[54];
        int attackCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int d = 0; d < 6; d++) {
                    attackCount += tryDoubleAttack(state, player, opponent, from, d, attackCount, neighbourIndicies, tempResult);
                }
            }
        }
        
        final BigInteger[] result = new BigInteger[attackCount];
        System.arraycopy(tempResult, 0, result, 0, attackCount);
        return result;
    }
    
    private static int tryDoubleAttack(BigInteger state, Player player, Player opponent, int from, int dir, int moveCount, int[] neighbourIndicies, BigInteger[] tempResult) {
        final int secondMarbleIndex = neighbourIndicies[dir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final Player secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndicies = adjacency(secondMarbleIndex);
        final int targetForSecondMarbleIndex = secondMarbleNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (targetForSecondMarbleIndex == -1) return 0;
        final Player targetForSecondMarble = lookAtField(state, targetForSecondMarbleIndex);
        // Das Feld hat keinen Gegner
        if (targetForSecondMarble != opponent) return 0;
        
        final int[] opponentNeighbourIndicies = adjacency(targetForSecondMarbleIndex);
        final int emptyPlaceForOpponentMarbleIndex = opponentNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das der Gegner gezogen werden kann:
        if (emptyPlaceForOpponentMarbleIndex == -1) return 0;
        final Player emptyPlaceForOpponentMarble = lookAtField(state, emptyPlaceForOpponentMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForOpponentMarble != EMPTY) return 0;
        
        state = move(state, opponent, targetForSecondMarbleIndex, emptyPlaceForOpponentMarbleIndex);
        state = move(state, player, secondMarbleIndex, targetForSecondMarbleIndex);
        state = move(state, player, from, secondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
