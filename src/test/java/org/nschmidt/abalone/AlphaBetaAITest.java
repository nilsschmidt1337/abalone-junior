package org.nschmidt.abalone;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlphaBetaAITest {

    @Test
    void testBestMoveDepthFourOfInitialField() {
        // Arrange
        AlphaBetaAI cut = new AlphaBetaAI(4, Player.WHITE);
        
        // Act
        Field move = cut.bestMove(Field.INITIAL_FIELD);
        move.printField();
        assertEquals(128L, FieldEvaluator.score(move, Player.WHITE));
    }
    
    @Test
    void testBestMoveDepthTwoOfInitialField() {
        // Arrange
        AlphaBetaAI cut = new AlphaBetaAI(2, Player.WHITE);
        
        // Act
        Field move = cut.bestMove(Field.INITIAL_FIELD);
        move.printField();
        assertEquals(139L, FieldEvaluator.score(move, Player.WHITE));
    }
}
