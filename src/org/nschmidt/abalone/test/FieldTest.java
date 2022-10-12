package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.*;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;

import java.math.BigInteger;

import static org.nschmidt.abalone.Player.EMPTY;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.Player;

class FieldTest {

    @Test
    void testInitialize() {
        assertEquals(BigInteger.valueOf(70368475742208L), INITIAL_FIELD);
    }
    
    @Test
    void testPopulateField() {
        final BigInteger state = populateField(INITIAL_FIELD, 0, BLACK);
        final Player target = lookAtField(state, 0);
        
        assertEquals(BLACK, target);
    }

    @Test
    void testMove() {
        final Player origin = lookAtField(INITIAL_FIELD, 0);
        final BigInteger state = move(INITIAL_FIELD, origin, 0, 21);
        
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
        Throwable ex = assertThrows(ArithmeticException.class, () -> 
            lookAtField(INITIAL_FIELD, -1)
        );
        
        assertEquals("Negative bit address", ex.getMessage());
    }

    @Test
    void testLookAtFieldForOffByOneIndex() {
        final Player playerOutOfBounds = lookAtField(INITIAL_FIELD, 74);
        assertEquals(EMPTY, playerOutOfBounds);
    }
}
