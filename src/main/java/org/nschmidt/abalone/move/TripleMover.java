package org.nschmidt.abalone.move;

import static org.nschmidt.abalone.playfield.Adjacency.adjacency;
import static org.nschmidt.abalone.playfield.Field.DIRECTION_COUNT;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT;
import static org.nschmidt.abalone.playfield.Field.lookAtField;
import static org.nschmidt.abalone.playfield.Field.move;
import static org.nschmidt.abalone.playfield.Player.EMPTY;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public enum TripleMover {
    INSTANCE;
    
    public static Field[] moveThreeMarbles(Field state, Player player) {
        final Field[] tempResult = new Field[PIECE_COUNT * DIRECTION_COUNT * 2];
        int moveCount = 0;
        
        for (int from = 0; from < FIELD_SIZE; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndices = adjacency(from);
                for (int d = 0; d < DIRECTION_COUNT; d++) {
                    for (int d2 = 0; d2 < DIRECTION_COUNT; d2++) {
                        moveCount += tryMove(state, player, from, d, d2, moveCount, neighbourIndices, tempResult);
                    }
                }
            }
        }
        
        final Field[] result = new Field[moveCount];
        System.arraycopy(tempResult, 0, result, 0, moveCount);
        return result;
    }

    private static int tryMove(Field state, Player player, int from, int dir, int nextPieceDir, int moveCount, int[] neighbourIndices, Field[] tempResult) {

        final int emptyPlaceIndex = neighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das gezogen werden kann:
        if (emptyPlaceIndex == -1) return 0;
        final Player emptyPlace = lookAtField(state, emptyPlaceIndex);
        // Das Feld ist nicht leer
        if (emptyPlace != EMPTY) return 0;
        
        final int secondMarbleIndex = neighbourIndices[nextPieceDir];
        // Es ist keine Murmel an dieser Stelle
        if (secondMarbleIndex == -1) return 0;
        final Player secondMarble = lookAtField(state, secondMarbleIndex);
        // Die zweite Murmel ist nicht vom selben Spieler
        if (secondMarble != player) return 0;
        
        final int[] secondMarbleNeighbourIndices = adjacency(secondMarbleIndex);
        final int emptyPlaceForSecondMarbleIndex = secondMarbleNeighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (emptyPlaceForSecondMarbleIndex == -1) return 0;
        final Player emptyPlaceForSecondMarble = lookAtField(state, emptyPlaceForSecondMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForSecondMarble != EMPTY && emptyPlaceForSecondMarbleIndex != from) return 0;
        
        final int thirdMarbleIndex = secondMarbleNeighbourIndices[nextPieceDir];
        // Es ist keine Murmel an dieser Stelle
        if (thirdMarbleIndex == -1) return 0;
        final Player thirdMarble = lookAtField(state, thirdMarbleIndex);
        // Die dritte Murmel ist nicht vom selben Spieler
        if (thirdMarble != player) return 0;
        
        final int[] thirdMarbleNeighbourIndices = adjacency(thirdMarbleIndex);
        final int emptyPlaceForThirdMarbleIndex = thirdMarbleNeighbourIndices[dir];
        // Die Richtung hat kein Feld, auf das die zweite Murmel gezogen werden kann:
        if (emptyPlaceForThirdMarbleIndex == -1) return 0;
        final Player emptyPlaceForThirdMarble = lookAtField(state, emptyPlaceForThirdMarbleIndex);
        // Das Feld ist nicht leer
        if (emptyPlaceForThirdMarble != EMPTY && emptyPlaceForThirdMarbleIndex != secondMarbleIndex) return 0;
        
        state = move(state, player, from, emptyPlaceIndex);
        state = move(state, player, secondMarbleIndex, emptyPlaceForSecondMarbleIndex);
        state = move(state, player, thirdMarbleIndex, emptyPlaceForThirdMarbleIndex);
        
        tempResult[moveCount] = state;
        
        return 1;
    }
}
