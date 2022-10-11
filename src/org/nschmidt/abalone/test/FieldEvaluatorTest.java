package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.FieldEvaluator.*;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;

import org.junit.jupiter.api.Test;

class FieldEvaluatorTest {

    @Test
    void testScoreOfInitialFieldForPlayer1() {
        assertEquals(score(INITIAL_FIELD, WHITE), score(INITIAL_FIELD, BLACK));
        assertEquals(-5850L, score(INITIAL_FIELD, BLACK));
    }
}
