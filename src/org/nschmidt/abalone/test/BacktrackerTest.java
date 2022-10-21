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
        assertEquals(Field.of(new Player[] {BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, WHITE, null, null, null, WHITE, EMPTY, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE})
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
    void testBacktrackGiveUp() {
        Field start = EMPTY_FIELD;
        start = populateField(start, 6, WHITE);
        start = populateField(start, 10, WHITE);
        start = populateField(start, 12, WHITE);
        start = populateField(start, 17, WHITE);
        start = populateField(start, 19, WHITE);
        start = populateField(start, 23, WHITE);
        start = populateField(start, 24, WHITE);
        start = populateField(start, 29, WHITE);
        start = populateField(start, 33, WHITE);
        
        start = populateField(start, 0, BLACK);
        start = populateField(start, 3, BLACK);
        start = populateField(start, 5, BLACK);
        start = populateField(start, 7, BLACK);
        start = populateField(start, 9, BLACK);
        start = populateField(start, 11, BLACK);
        start = populateField(start, 13, BLACK);
        start = populateField(start, 18, BLACK);
        start = populateField(start, 20, BLACK);
        Field result = backtrack(start, BLACK, 10);
        result.printFieldDelta(start);
        assertTrue(WinningChecker.wins(result, WHITE));
    }
    
    @Test
    void testAvoidIsolation() {
        Field start = Field.of(new Player[] {BLACK, EMPTY, BLACK, EMPTY, EMPTY, BLACK, BLACK, EMPTY, EMPTY, null, null, BLACK, WHITE, BLACK, BLACK, null, null, WHITE, WHITE, BLACK, BLACK, null, null, WHITE, WHITE, WHITE, WHITE, null, EMPTY, EMPTY, WHITE, WHITE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY});
        start.printField();
        Field result = backtrack(start, WHITE, 10);
        result.printFieldDelta(start);
    }
    
    @Test
    void testAvoidDoingNothing() {
        Field start = Field.of(new Player[] {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WHITE, BLACK, BLACK, EMPTY, null, BLACK, WHITE, BLACK, EMPTY, BLACK, null, WHITE, BLACK, BLACK, WHITE, BLACK, EMPTY, EMPTY, WHITE, BLACK, WHITE, WHITE, null, EMPTY, WHITE, EMPTY, WHITE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY});
        start.printField();
        Field result = backtrack(start, WHITE, 10);
        result.printFieldDelta(start);
    }
}
