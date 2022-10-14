package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Backtracker.backtrack;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.Field.EMPTY_FIELD;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;
import static org.nschmidt.abalone.Player.EMPTY;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.Field;
import org.nschmidt.abalone.Player;
import org.nschmidt.abalone.WinningChecker;

class BacktrackerTest {

    @Test
    void testBacktrack() {
        Field result = backtrack(INITIAL_FIELD, WHITE, 10);
        assertEquals(Field.of(new Player[] {BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, WHITE, WHITE, WHITE, null, null, EMPTY, EMPTY, EMPTY, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE})
        , result);
    }
    
    /*
        0  1  2  3
       4  5  6  7  8
      9 10 11 12 13 14
    15 16 17 18 19 20 21
      22 23 24 25 26 27
       28 29 30 31 32
        33 34 35 36
     */
    @Test
    void testBacktrackMistake() {
        Field start = EMPTY_FIELD;
        start = populateField(start, 36, WHITE);
        start = populateField(start, 34, WHITE);
        start = populateField(start, 33, WHITE);
        start = populateField(start, 30, WHITE);
        start = populateField(start, 25, WHITE);
        start = populateField(start, 18, WHITE);
        start = populateField(start, 11, WHITE);
        start = populateField(start, 12, WHITE);
        start = populateField(start, 7, WHITE);
        
        start = populateField(start, 2, BLACK);
        start = populateField(start, 3, BLACK);
        start = populateField(start, 5, BLACK);
        start = populateField(start, 6, BLACK);
        start = populateField(start, 8, BLACK);
        start = populateField(start, 10, BLACK);
        start = populateField(start, 17, BLACK);
        start = populateField(start, 19, BLACK);
        start = populateField(start, 23, BLACK);
        Field result = backtrack(start, BLACK, 10);
        assertFalse(WinningChecker.wins(result, WHITE));
    }
}
