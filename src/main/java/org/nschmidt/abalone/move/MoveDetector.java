package org.nschmidt.abalone.move;

import static org.nschmidt.abalone.move.DoubleAttacker.performDoubleAttack;
import static org.nschmidt.abalone.move.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.move.SingleMover.moveSingleMarble;
import static org.nschmidt.abalone.move.TripleAttacker.performTripleAttack;
import static org.nschmidt.abalone.move.TripleMover.moveThreeMarbles;
import static org.nschmidt.abalone.move.TripleToDoubleAttacker.performTripleToDoubleAttack;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

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
