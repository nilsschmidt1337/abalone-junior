package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.move;
import static org.nschmidt.abalone.Player.EMPTY;

import java.math.BigInteger;

public enum SingleMover {
    INSTANCE;
    
    public static BigInteger[] moveSingleMarble(BigInteger state, Player player) {
        final BigInteger[] tempResult = new BigInteger[54];
        int moveCount = 0;
        
        for (int from = 0; from < 37; from++) {
            if (player == lookAtField(state, from)) {
                final int[] neighbourIndicies = adjacency(from);
                for (int to : neighbourIndicies) {
                    if (to == -1 || EMPTY != lookAtField(state, to)) continue;
                    tempResult[moveCount] = move(state, player, from, to);
                    moveCount++;
                }
            }
        }
        
        final BigInteger[] result = new BigInteger[moveCount];
        System.arraycopy(tempResult, 0, result, 0, moveCount);
        return result;
    }
}
