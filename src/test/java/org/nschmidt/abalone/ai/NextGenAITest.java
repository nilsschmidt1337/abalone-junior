package org.nschmidt.abalone.ai;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.playfield.Field.EMPTY_FIELD;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.populateField;
import static org.nschmidt.abalone.playfield.NextGenFieldEvaluator.score;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

class NextGenAITest {
    
    private static final Random RND = new Random(1337L);
    
    @Test
    void testOldAiVsNewAiOnInitialField() {
        // Arrange
        // Act
        Field newMove = NextGenAI.bestMove(Field.INITIAL_FIELD, Player.WHITE);
        Field oldMove = NextGenAIOld.bestMove(Field.INITIAL_FIELD, Player.WHITE);
        
        // Assert
        int oldScore = score(oldMove, Player.WHITE);
        int newScore = score(newMove, Player.WHITE);
        assertEquals(oldScore, newScore);
    }
    
    @Test
    void testOldAiVsNewAiOnRandomField() {
        // Arrange
        Field randomField = EMPTY_FIELD;
        for (int i = 0; i < 96; i++) {
            if (RND.nextBoolean()) {
                randomField = populateField(randomField, RND.nextInt(FIELD_SIZE), WHITE);
            } else {
                randomField = populateField(randomField, RND.nextInt(FIELD_SIZE), BLACK);
            }
        }
        
        // Act
        Field newMove = NextGenAI.bestMove(randomField, Player.WHITE);
        Field oldMove = NextGenAIOld.bestMove(randomField, Player.WHITE);
        
        // Assert
        int oldScore = score(oldMove, Player.WHITE);
        int newScore = score(newMove, Player.WHITE);
        assertEquals(oldScore, newScore);
    }
}
