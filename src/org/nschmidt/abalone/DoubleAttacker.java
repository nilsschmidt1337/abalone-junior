package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.FIELD_SIZE;
import static org.nschmidt.abalone.Field.PIECE_COUNT;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;
import static org.nschmidt.abalone.Player.EMPTY;

public enum DoubleAttacker {
    INSTANCE;
    
    public static Field[] performDoubleAttack(Field state, Player player) {
        final Field[] tempResult = new Field[PIECE_COUNT * 6];
        int attackCount = 0;
        
        for (int from = 0; from < FIELD_SIZE; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndices = adjacency(from);
                for (int d = 0; d < 6; d++) {
                    attackCount += tryDoubleAttack(state, player, from, d, attackCount, neighbourIndices, tempResult);
                }
            }
        }
        
        final Field[] result = new Field[attackCount];
        System.arraycopy(tempResult, 0, result, 0, attackCount);
        return result;
    }
    
    private static int tryDoubleAttack(Field state, Player player, int from, int dir, int moveCount, int[] neighbourIndices, Field[] tempResult) {
        final int secondMarbleIndex = neighbourIndices[dir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final Player secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndices = adjacency(secondMarbleIndex);
        final int targetForSecondMarbleIndex = secondMarbleNeighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (targetForSecondMarbleIndex == -1) return 0;
        final Player targetForSecondMarble = lookAtField(state, targetForSecondMarbleIndex);
        // Das Feld hat keinen Gegner
        if (targetForSecondMarble == EMPTY || targetForSecondMarble == player) return 0;
        
        final int[] opponentNeighbourIndices = adjacency(targetForSecondMarbleIndex);
        final int emptyPlaceForOpponentMarbleIndex = opponentNeighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das der Gegner gezogen werden kann:
        if (emptyPlaceForOpponentMarbleIndex == -1) return 0;
        final Player emptyPlaceForOpponentMarble = lookAtField(state, emptyPlaceForOpponentMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForOpponentMarble != EMPTY) return 0;
        
        state = move(state, targetForSecondMarble, targetForSecondMarbleIndex, emptyPlaceForOpponentMarbleIndex);
        state = move(state, player, secondMarbleIndex, targetForSecondMarbleIndex);
        state = move(state, player, from, secondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
