package org.nschmidt.abalone.winning;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.playfield.Field.EMPTY_FIELD;
import static org.nschmidt.abalone.playfield.Field.populateField;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.EMPTY;
import static org.nschmidt.abalone.playfield.Player.WHITE;
import static org.nschmidt.abalone.winning.WinningInTwoMovesChecker.winsInTwoMoves;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

class WinningInTwoMovesCheckerTest {

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
    void testWinsInTwoMoves() {
        Field start = EMPTY_FIELD;
        start = populateField(start, 0, BLACK);
        start = populateField(start, 2, WHITE);
        start = populateField(start, 3, WHITE);
        start = populateField(start, 4, WHITE);
        start = populateField(start, 5, WHITE);
        start = populateField(start, 6, WHITE);
        start = populateField(start, 7, WHITE);
        Field[] result = winsInTwoMoves(start, WHITE);
        assertEquals(2, result.length);
    }
    
    @Test
    void testWinsInTwo() {
        Field start = Field.of(new Player[] {EMPTY, BLACK, BLACK, BLACK, WHITE, BLACK, EMPTY, BLACK, BLACK, BLACK, EMPTY, EMPTY, EMPTY, null, null, EMPTY, EMPTY, BLACK, BLACK, null, null, null, EMPTY, null, null, null, null, null, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY});
        Field[] result = winsInTwoMoves(start, BLACK);
        assertEquals(2, result.length);
    }
}
