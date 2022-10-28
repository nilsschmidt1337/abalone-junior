package org.nschmidt.abalone.ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.FieldEvaluator;
import org.nschmidt.abalone.playfield.Player;

class AlphaBetaAITest {

    @Test
    void testBestMoveDepthFourOfInitialField() {
        // Arrange
        AlphaBetaAI cut = new AlphaBetaAI(4, Player.WHITE);
        
        // Act
        Field move = cut.bestMove(Field.INITIAL_FIELD);
        
        // Assert
        assertEquals(128L, FieldEvaluator.score(move, Player.WHITE));
    }
    
    @Test
    void testBestMoveDepthTwoOfInitialField() {
        // Arrange
        AlphaBetaAI cut = new AlphaBetaAI(2, Player.WHITE);
        
        // Act
        Field move = cut.bestMove(Field.INITIAL_FIELD);

        // Assert
        assertEquals(139L, FieldEvaluator.score(move, Player.WHITE));
    }
}
