package org.nschmidt.abalone.move;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.move.TripleMover.moveThreeMarbles;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.playfield.Field;

class TripleMoverTest {

    @Test
    void testMoveThreeMarbles1() {
        final Field state = INITIAL_FIELD;
        Field[] moves = moveThreeMarbles(state, WHITE);
        assertEquals(12, moves.length);
    }
    
    @Test
    void testMoveThreeMarbles2() {
        final Field state = INITIAL_FIELD;
        Field[] moves = moveThreeMarbles(state, BLACK);
        assertEquals(12, moves.length);
    }
}
