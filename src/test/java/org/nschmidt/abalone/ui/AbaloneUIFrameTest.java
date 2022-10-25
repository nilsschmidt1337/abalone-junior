package org.nschmidt.abalone.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AbaloneUIFrameTest {

    @Test
    @Disabled
    void testAbaloneUIFrame() {
        AbaloneUIWindowsFrame frame = new AbaloneUIWindowsFrame(INITIAL_FIELD, WHITE);
        assertEquals(INITIAL_FIELD, frame.getCurrentState());
    }
    
    @Test
    @Disabled
    void testAbaloneUIMacOSXFrame() {
        AbaloneUIMacOSXFrame frame = new AbaloneUIMacOSXFrame(INITIAL_FIELD, WHITE);
        assertEquals(INITIAL_FIELD, frame.getCurrentState());
    }
}
