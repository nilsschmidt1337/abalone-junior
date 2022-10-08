package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.AbaloneUIFrame;

class AbaloneUIFrameTest {

    @Test
    void testAbaloneUIFrame() {
        AbaloneUIFrame frame = new AbaloneUIFrame(INITIAL_FIELD, 1L);
        assertEquals(INITIAL_FIELD, frame.getCurrentState());
    }

}
