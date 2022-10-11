package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Player.WHITE;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.AbaloneUIFrame;
import org.nschmidt.abalone.AbaloneUIMacOSXFrame;

class AbaloneUIFrameTest {

    @Test
    void testAbaloneUIFrame() {
        AbaloneUIFrame frame = new AbaloneUIFrame(INITIAL_FIELD, WHITE);
        assertEquals(INITIAL_FIELD, frame.getCurrentState());
    }
    
    @Test
    void testAbaloneUIMacOSXFrame() {
        AbaloneUIMacOSXFrame frame = new AbaloneUIMacOSXFrame(INITIAL_FIELD, WHITE);
        assertEquals(INITIAL_FIELD, frame.getCurrentState());
    }
}
