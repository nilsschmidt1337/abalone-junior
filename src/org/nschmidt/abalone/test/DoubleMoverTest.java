package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.Player.BLACK;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class DoubleMoverTest {

    @Test
    void testMoveTwoMarbles1() {
        final BigInteger state = BigInteger.valueOf(104340773383259005L);
        BigInteger[] moves = moveTwoMarbles(state, BLACK);
        assertEquals(22, moves.length);
    }
    
    @Test
    void testMoveTwoMarbles2() {
        final BigInteger state = BigInteger.valueOf(1804882274845767L);
        BigInteger[] moves = moveTwoMarbles(state, BLACK);
        assertEquals(26, moves.length);
    }
}
