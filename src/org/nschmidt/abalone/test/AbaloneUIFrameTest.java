package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Player.WHITE;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.AbaloneUIWindowsFrame;
import org.nschmidt.abalone.AbaloneUIMacOSXFrame;

class AbaloneUIFrameTest {

    @Test
    void testAbaloneUIFrame() {
        AbaloneUIWindowsFrame frame = new AbaloneUIWindowsFrame(INITIAL_FIELD, WHITE);
        assertEquals(INITIAL_FIELD, frame.getCurrentState());
    }
    
    @Test
    void testAbaloneUIMacOSXFrame() {
        AbaloneUIMacOSXFrame frame = new AbaloneUIMacOSXFrame(INITIAL_FIELD, WHITE);
        assertEquals(INITIAL_FIELD, frame.getCurrentState());
    }
}
