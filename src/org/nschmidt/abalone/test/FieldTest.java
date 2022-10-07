package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.*;

import org.junit.jupiter.api.Test;

class FieldTest {

    @Test
    void testInitialize() {
        assertEquals(450261029098552243L, INITIAL_FIELD);
    }
    
    @Test
    void testPopulateField() {
        final long state = populateField(INITIAL_FIELD, 0, 2L);
        final long target = lookAtField(state, 0);
        
        assertEquals(2L, target);
    }

    @Test
    void testMove() {
        final long origin = lookAtField(INITIAL_FIELD, 0);
        final long state = move(INITIAL_FIELD, origin, 0, 21);
        
        assertEquals(origin, lookAtField(state, 21));
        assertEquals(0, lookAtField(state, 0));
    }

    @Test
    void testLookAtField() {
        assertEquals(1L, lookAtField(INITIAL_FIELD, 0));
        assertEquals(2L, lookAtField(INITIAL_FIELD, 36));
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
        Throwable ex = assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            lookAtField(INITIAL_FIELD, 38)
        );
       
        assertEquals("Index 38 out of bounds for length 37", ex.getMessage());
    }

}
