package org.nschmidt.abalone.playfield;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.ai.NextGenAIOld.evalGameField;
import static org.nschmidt.abalone.playfield.Field.EMPTY_FIELD;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Field.populateField;
import static org.nschmidt.abalone.playfield.NextGenFieldEvaluator.*;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import java.util.Random;

import org.junit.jupiter.api.Test;

class NextGenFieldEvaluatorTest {

    private static final Random RND = new Random();
    
    @Test
    void testScoreOfEmptyField() {
        assertEquals(0, score(EMPTY_FIELD, WHITE));
        assertEquals(0, evalGameField(EMPTY_FIELD, WHITE));
        assertEquals(0, score(EMPTY_FIELD, BLACK));
        assertEquals(0, evalGameField(EMPTY_FIELD, BLACK));
    }
    
    @Test
    void testScoreOfInitialField() {
        if (Field.FIELD_SIZE > 37) {
            assertEquals(0, score(INITIAL_FIELD, WHITE));
            assertEquals(0, evalGameField(INITIAL_FIELD, WHITE));
            assertEquals(0, score(INITIAL_FIELD, BLACK));
            assertEquals(0, evalGameField(INITIAL_FIELD, BLACK));
        }
    }
    
    @Test
    void testScoreOfFieldWithOnePieceWHITE() {
        if (Field.FIELD_SIZE > 37) {
            for (int i = 0; i < 1000; i++) {
                final Field randomPiece = populateField(EMPTY_FIELD, RND.nextInt(FIELD_SIZE), WHITE);
                assertEquals(evalGameField(randomPiece, WHITE), score(randomPiece, WHITE));
            }
        }
    }
    
    @Test
    void testScoreOfFieldWithOnePieceBLACK() {
        if (Field.FIELD_SIZE > 37) {
            for (int i = 0; i < 1000; i++) {
                final Field randomPiece = populateField(EMPTY_FIELD, RND.nextInt(FIELD_SIZE), BLACK);
                assertEquals(evalGameField(randomPiece, BLACK), score(randomPiece, BLACK));
            }
        }
    }
    
    @Test
    void testScoreOfRandomHighPopulationField() {
        if (Field.FIELD_SIZE > 37) {
            for (int k = 0; k < 100000; k++) {
                Field randomField = EMPTY_FIELD;
                for (int i = 0; i < 96; i++) {
                    if (RND.nextBoolean()) {
                        randomField = populateField(randomField, RND.nextInt(FIELD_SIZE), WHITE);
                    } else {
                        randomField = populateField(randomField, RND.nextInt(FIELD_SIZE), BLACK);
                    }
                }
            
                assertEquals(evalGameField(randomField, WHITE), score(randomField, WHITE));
                assertEquals(evalGameField(randomField, BLACK), score(randomField, BLACK));
            }
        }
    }
    
    @Test
    void testScoreOfRandomLowPopulationField() {
        if (Field.FIELD_SIZE > 37) {
            for (int k = 0; k < 1000; k++) {
                Field randomField = EMPTY_FIELD;
                for (int i = 0; i < 32; i++) {
                    if (RND.nextBoolean()) {
                        randomField = populateField(randomField, RND.nextInt(FIELD_SIZE), WHITE);
                    } else {
                        randomField = populateField(randomField, RND.nextInt(FIELD_SIZE), BLACK);
                    }
                }
                
                assertEquals(evalGameField(randomField, WHITE), score(randomField, WHITE));
                assertEquals(evalGameField(randomField, BLACK), score(randomField, BLACK));
            }
        }
    }
}
