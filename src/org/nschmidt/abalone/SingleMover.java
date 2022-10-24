package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.FIELD_SIZE;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;
import static org.nschmidt.abalone.Player.EMPTY;

public enum SingleMover {
    INSTANCE;
    
    public static Field[] moveSingleMarble(Field state, Player player) {
        final Field[] tempResult = new Field[54];
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
