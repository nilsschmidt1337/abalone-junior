package org.nschmidt.abalone.move;

import static org.nschmidt.abalone.playfield.Adjacency.adjacency;
import static org.nschmidt.abalone.playfield.Field.DIRECTION_COUNT;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT_FOR_WIN;
import static org.nschmidt.abalone.playfield.Field.lookAtField;
import static org.nschmidt.abalone.playfield.Field.move;
import static org.nschmidt.abalone.playfield.Player.EMPTY;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

enum TripleToDoubleAttacker {
    INSTANCE;
    
    public static Field[] performTripleToDoubleAttack(Field state, Player player) {
        final Field[] tempResult = new Field[PIECE_COUNT * DIRECTION_COUNT];
        int attackCount = 0;
        
        for (int from = 0; from < FIELD_SIZE; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndices = adjacency(from);
                for (int d = 0; d < DIRECTION_COUNT; d++) {
                    attackCount += tryTripleToDoubleAttack(state, player, from, d, attackCount, neighbourIndices, tempResult);
                }
            }
        }
        
        final Field[] result = new Field[attackCount];
        System.arraycopy(tempResult, 0, result, 0, attackCount);
        return result;
    }
    
    private static int tryTripleToDoubleAttack(Field state, Player player, int from, int dir, int moveCount, int[] neighbourIndices, Field[] tempResult) {
        final int secondMarbleIndex = neighbourIndices[dir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final Player secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndices = adjacency(secondMarbleIndex);
        final int thirdMarbleIndex = secondMarbleNeighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (thirdMarbleIndex == -1) return 0;
        final Player targetForSecondMarble = lookAtField(state, thirdMarbleIndex);
        // Die dritte Murmel ist nicht vom selben Spieler
        if (targetForSecondMarble != player) return 0;
        
        final int[] thirdMarbleNeighbourIndices = adjacency(thirdMarbleIndex);
        final int fourthMarbleIndex = thirdMarbleNeighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (fourthMarbleIndex == -1) return 0;
        final Player targetForThirdMarble = lookAtField(state, fourthMarbleIndex);
        // Das Feld hat keinen Gegner
        if (targetForThirdMarble == EMPTY || targetForThirdMarble == player) return 0;
        
        final int[] fourthMarbleNeighbourIndices = adjacency(fourthMarbleIndex);
        final int targetForFourthMarbleIndex = fourthMarbleNeighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das die vierte Murmel gezogen werden kann:
        if (targetForFourthMarbleIndex == -1) return 0;
        final Player targetForForthMarble = lookAtField(state, targetForFourthMarbleIndex);
        // Das Feld hat keinen Gegner
        if (targetForForthMarble == EMPTY || targetForForthMarble == player) return 0;
        
        final int[] opponentNeighbourIndices = adjacency(targetForFourthMarbleIndex);
        final int emptyPlaceForOpponentMarbleIndex = opponentNeighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das der Gegner gezogen werden kann:
        if (emptyPlaceForOpponentMarbleIndex == -1) {
            if (PIECE_COUNT_FOR_WIN > 1) {
                int lostPieces = PIECE_COUNT - Field.countPieces(state, player.switchPlayer()) + 1;
                if (lostPieces < PIECE_COUNT_FOR_WIN) {
                    state = move(state, targetForThirdMarble, fourthMarbleIndex, targetForFourthMarbleIndex);
                    state = move(state, player, thirdMarbleIndex, fourthMarbleIndex);
                    state = move(state, player, secondMarbleIndex, thirdMarbleIndex);
                    state = move(state, player, from, secondMarbleIndex);
                    tempResult[moveCount] = state;
                    
                    return 1;
                }
            }
            
            return 0;
        }
        final Player emptyPlaceForOpponentMarble = lookAtField(state, emptyPlaceForOpponentMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForOpponentMarble != EMPTY) return 0;
        
        state = move(state, targetForForthMarble, targetForFourthMarbleIndex, emptyPlaceForOpponentMarbleIndex);
        state = move(state, targetForThirdMarble, fourthMarbleIndex, targetForFourthMarbleIndex);
        state = move(state, player, thirdMarbleIndex, fourthMarbleIndex);
        state = move(state, player, secondMarbleIndex, thirdMarbleIndex);
        state = move(state, player, from, secondMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
