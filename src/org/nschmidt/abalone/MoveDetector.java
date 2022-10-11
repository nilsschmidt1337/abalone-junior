package org.nschmidt.abalone;

import static org.nschmidt.abalone.DoubleAttacker.performDoubleAttack;
import static org.nschmidt.abalone.TripleAttacker.performTripleAttack;
import static org.nschmidt.abalone.TripleToDoubleAttacker.performTripleToDoubleAttack;

import java.math.BigInteger;

import static org.nschmidt.abalone.TripleMover.moveThreeMarbles;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.SingleMover.moveSingleMarble;

public enum MoveDetector {
    INSTANCE;
    
    public static BigInteger[] allMoves(BigInteger state, Player player) {
        return join(moveSingleMarble(state, player), moveTwoMarbles(state, player), 
                    moveThreeMarbles(state, player), performDoubleAttack(state, player),
                    performTripleAttack(state, player), performTripleToDoubleAttack(state, player));
    }
    
    private static BigInteger[] join(BigInteger[]... arrays) {
        int length = 0;
        for (BigInteger[] array : arrays) {
            length += array.length;
        }

        final BigInteger[] result = new BigInteger[length];

        int offset = 0;
        for (BigInteger[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }
}
