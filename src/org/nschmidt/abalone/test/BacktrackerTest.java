package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Backtracker.backtrack;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;

import org.junit.jupiter.api.Test;

class BacktrackerTest {

    @Test
    void testBacktrack() {
        long result = backtrack(INITIAL_FIELD, 1L, 10);
        assertEquals(224834340108701141L, result);
    }

}
