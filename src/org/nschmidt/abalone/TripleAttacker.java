package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;
import static org.nschmidt.abalone.Player.EMPTY;

public enum TripleAttacker {
    INSTANCE;
    
    public static Field[] performTripleAttack(Field state, Player player) {
        final Field[] tempResult = new Field[54];
        int attackCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int d = 0; d < 6; d++) {
                    attackCount += tryTripleAttack(state, player, from, d, attackCount, neighbourIndicies, tempResult);
                }
            }
        }
        
        final Field[] result = new Field[attackCount];
        System.arraycopy(tempResult, 0, result, 0, attackCount);
        return result;
    }
    
    private static int tryTripleAttack(Field state, Player player, int from, int dir, int moveCount, int[] neighbourIndicies, Field[] tempResult) {
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
        if (targetForThirdMarble == EMPTY || targetForThirdMarble == player) return 0;
        
        final int[] opponentNeighbourIndicies = adjacency(targetForThirdMarbleIndex);
        final int emptyPlaceForOpponentMarbleIndex = opponentNeighbourIndicies[dir];
        // Die Richtung hat kein Feld, auf das der Gegner gezogen werden kann:
        if (emptyPlaceForOpponentMarbleIndex == -1) return 0;
        final Player emptyPlaceForOpponentMarble = lookAtField(state, emptyPlaceForOpponentMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForOpponentMarble != EMPTY) return 0;
        
        state = move(state, targetForThirdMarble, targetForThirdMarbleIndex, emptyPlaceForOpponentMarbleIndex);
        state = move(state, player, thirdMarbleIndex, targetForThirdMarbleIndex);
        state = move(state, player, secondMarbleIndex, thirdMarbleIndex);
        state = move(state, player, from, secondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
