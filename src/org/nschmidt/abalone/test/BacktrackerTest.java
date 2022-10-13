package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Backtracker.backtrack;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;
import static org.nschmidt.abalone.Player.EMPTY;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.Field;
import org.nschmidt.abalone.Player;

class BacktrackerTest {

    @Test
    void testBacktrack() {
        Field result = backtrack(INITIAL_FIELD, WHITE, 10);
        assertEquals(Field.of(new Player[] {BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, WHITE, WHITE, WHITE, null, null, EMPTY, EMPTY, EMPTY, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE})
        , result);
    }
}
