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

enum SingleMover {
    INSTANCE;
    
    public static Field[] moveSingleMarble(Field state, Player player) {
        final Field[] tempResult = new Field[PIECE_COUNT * DIRECTION_COUNT];
        int moveCount = 0;
        
        for (int from = 0; from < FIELD_SIZE; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndices = adjacency(from);
                for (int to : neighbourIndices) {
                    if (to == -1 || EMPTY != lookAtField(state, to)) continue;
                    tempResult[moveCount] = move(state, player, from, to);
                    moveCount++;
                }
            }
        }
        
        final Field[] result = new Field[moveCount];
        System.arraycopy(tempResult, 0, result, 0, moveCount);
        return result;
    }
}
