package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.TripleMover.moveThreeMarbles;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class TripleMoverTest {

    @Test
    void testMoveThreeMarbles1() {
        final BigInteger state = BigInteger.valueOf(104340773383259005L);
        BigInteger[] moves = moveThreeMarbles(state, BLACK);
        assertEquals(6, moves.length);
    }
    
    @Test
    void testMoveThreeMarbles2() {
        final BigInteger state = BigInteger.valueOf(1804882274845767L);
        BigInteger[] moves = moveThreeMarbles(state, BLACK);
        assertEquals(6, moves.length);
    }
}
