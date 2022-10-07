package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;

import org.junit.jupiter.api.Test;

class DoubleMoverTest {

    @Test
    void testMoveTwoMarbles1() {
        final long state = 104340773383259005L;
        long[] moves = moveTwoMarbles(state, 2L);
        assertEquals(22, moves.length);
    }
    
    @Test
    void testMoveTwoMarbles2() {
        final long state = 1804882274845767L;
        long[] moves = moveTwoMarbles(state, 2L);
        assertEquals(26, moves.length);
    }
}
