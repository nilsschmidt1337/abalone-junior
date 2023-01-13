package org.nschmidt.abalone.ai;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.ai.Backtracker.backtrack;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.EMPTY;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

class BacktrackerTest {

    @Test
    void testBacktrack() {
        Field result = backtrack(INITIAL_FIELD, WHITE);
        
        if (Field.FIELD_SIZE > 37) {
            assertEquals(Field.of(new Player[] {WHITE, WHITE, null, BLACK, BLACK, WHITE, WHITE, WHITE, BLACK, BLACK, BLACK, null, WHITE, WHITE, null, BLACK, BLACK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, WHITE, null, null, null, null, BLACK, BLACK, null, WHITE, WHITE, null, BLACK, BLACK, BLACK, WHITE, WHITE, WHITE, BLACK, BLACK, null, WHITE, EMPTY}), result);
        } else {
            assertEquals(Field.of(new Player[] {WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, EMPTY, EMPTY, EMPTY, null, null, WHITE, WHITE, WHITE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK}), result);
        }
    }
}
