package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Backtracker.backtrack;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;

import org.junit.jupiter.api.Test;

class BacktrackerTest {

    @Test
    void testBacktrack() {
        long result = backtrack(INITIAL_FIELD, 1L, 10);
        assertEquals(225039383952186347L, result);
    }

}
