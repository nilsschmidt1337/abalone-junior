package org.nschmidt.abalone.playfield;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.playfield.Field.*;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.EMPTY;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import org.junit.jupiter.api.Test;

class FieldTest {

    @Test
    void testInitialize() {
        assertEquals(Field.of(new Player[] {BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE}), INITIAL_FIELD);
    }
    
    @Test
    void testPopulateField() {
        final Field state = populateField(INITIAL_FIELD, 0, BLACK);
        final Player target = lookAtField(state, 0);
        
        assertEquals(BLACK, target);
    }

    @Test
    void testMove() {
        final Player origin = lookAtField(INITIAL_FIELD, 0);
        final Field state = move(INITIAL_FIELD, origin, 0, 21);
        
        assertEquals(origin, lookAtField(state, 21));
        assertEquals(EMPTY, lookAtField(state, 0));
    }

    @Test
    void testLookAtField() {
        assertEquals(BLACK, lookAtField(INITIAL_FIELD, 0));
        assertEquals(WHITE, lookAtField(INITIAL_FIELD, 36));
    }
    
    @Test
    void testLookAtFieldForNegativeIndex() {
        final Player playerOutOfBounds = lookAtField(INITIAL_FIELD, -1);
        assertEquals(EMPTY, playerOutOfBounds);
    }

    @Test
    void testLookAtFieldForOffByOneIndex() {
        final Player playerOutOfBounds = lookAtField(INITIAL_FIELD, 37);
        assertEquals(EMPTY, playerOutOfBounds);
    }
}
