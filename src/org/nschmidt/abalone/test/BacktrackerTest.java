package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Backtracker.backtrack;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Player.WHITE;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class BacktrackerTest {

    @Test
    void testBacktrack() {
        BigInteger result = backtrack(INITIAL_FIELD, WHITE, 10);
        assertEquals(BigInteger.valueOf(70366655414272L), result);
    }

}
