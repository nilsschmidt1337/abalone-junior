package org.nschmidt.abalone;

import static org.nschmidt.abalone.DoubleAttacker.performDoubleAttack;
import static org.nschmidt.abalone.TripleAttacker.performTripleAttack;
import static org.nschmidt.abalone.TripleToDoubleAttacker.performTripleToDoubleAttack;

import static org.nschmidt.abalone.TripleMover.moveThreeMarbles;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.SingleMover.moveSingleMarble;

public enum MoveDetector {
    INSTANCE;
    
    public static Field[] allMoves(Field state, Player player) {
        return join(moveSingleMarble(state, player), moveTwoMarbles(state, player), 
                    moveThreeMarbles(state, player), performDoubleAttack(state, player),
                    performTripleAttack(state, player), performTripleToDoubleAttack(state, player));
    }
    
    public static Field[] allAttackMoves(Field state, Player player) {
        return join(performDoubleAttack(state, player), performTripleAttack(state, player), performTripleToDoubleAttack(state, player));
    }
    
    private static Field[] join(Field[]... arrays) {
        int length = 0;
        for (Field[] array : arrays) {
            length += array.length;
        }

        final Field[] result = new Field[length];

        int offset = 0;
        for (Field[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }
}
