package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.FieldEvaluator.*;

import org.junit.jupiter.api.Test;

class FieldEvaluatorTest {

    @Test
    void testScoreOfInitialFieldForPlayer1() {
        assertEquals(score(INITIAL_FIELD, 1L), score(INITIAL_FIELD, 2L));
        assertEquals(-5850L, score(INITIAL_FIELD, 2L));
    }
}
