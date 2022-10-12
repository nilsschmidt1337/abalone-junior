package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;
import static org.nschmidt.abalone.TripleMover.moveThreeMarbles;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class TripleMoverTest {

    @Test
    void testMoveThreeMarbles1() {
        final BigInteger state = INITIAL_FIELD;
        BigInteger[] moves = moveThreeMarbles(state, WHITE);
        assertEquals(12, moves.length);
    }
    
    @Test
    void testMoveThreeMarbles2() {
        final BigInteger state = INITIAL_FIELD;
        BigInteger[] moves = moveThreeMarbles(state, BLACK);
        assertEquals(12, moves.length);
    }
}
