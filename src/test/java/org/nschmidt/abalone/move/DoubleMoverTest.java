package org.nschmidt.abalone.move;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.move.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.playfield.Field;

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
