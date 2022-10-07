package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;

public enum SingleMover {
    INSTANCE;
    
    public static long[] moveSingleMarble(long state, long player) {
        final long[] tempResult = new long[54];
        int moveCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int to : neighbourIndicies) {
                    if (to == -1 || 0 != lookAtField(state, to)) continue;
                    tempResult[moveCount] = move(state, player, from, to);
                    moveCount++;
                }
            }
        }
        
        final long[] result = new long[moveCount];
        System.arraycopy(tempResult, 0, result, 0, moveCount);
        return result;
    }
}
