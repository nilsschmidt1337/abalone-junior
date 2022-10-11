package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.TripleMover.moveThreeMarbles;

import org.junit.jupiter.api.Test;

class TripleMoverTest {

    @Test
    void testMoveThreeMarbles1() {
        final long state = 104340773383259005L;
        long[] moves = moveThreeMarbles(state, BLACK);
        assertEquals(6, moves.length);
    }
    
    @Test
    void testMoveThreeMarbles2() {
        final long state = 1804882274845767L;
        long[] moves = moveThreeMarbles(state, BLACK);
        assertEquals(6, moves.length);
    }
}
