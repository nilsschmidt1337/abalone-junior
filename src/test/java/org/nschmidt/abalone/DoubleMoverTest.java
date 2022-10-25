package org.nschmidt.abalone;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;

import org.junit.jupiter.api.Test;

class DoubleMoverTest {

    @Test
    void testMoveTwoMarbles1() {
        final Field state = INITIAL_FIELD;
        Field[] moves = moveTwoMarbles(state, BLACK);
        assertEquals(24, moves.length);
    }
    
    @Test
    void testMoveTwoMarbles2() {
        final Field state = INITIAL_FIELD;
        Field[] moves = moveTwoMarbles(state, WHITE);
        assertEquals(24, moves.length);
    }
}
