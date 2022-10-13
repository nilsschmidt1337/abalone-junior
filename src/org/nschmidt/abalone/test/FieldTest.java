package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.*;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;

import static org.nschmidt.abalone.Player.EMPTY;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.Field;
import org.nschmidt.abalone.Player;

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
        Throwable ex = assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            lookAtField(INITIAL_FIELD, -1)
        );
        
        assertEquals("Index -1 out of bounds for length 37", ex.getMessage());
    }

    @Test
    void testLookAtFieldForOffByOneIndex() {
        final Player playerOutOfBounds = lookAtField(INITIAL_FIELD, 37);
        assertEquals(EMPTY, playerOutOfBounds);
    }
}
