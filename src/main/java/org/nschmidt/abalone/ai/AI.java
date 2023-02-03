package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.playfield.FieldEvaluator.score;

import java.util.Arrays;
import java.util.Random;

import org.nschmidt.abalone.move.MoveDetector;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public enum AI {
    INSTANCE;
    
    private static final Random RND = new Random(1337L);
    
    public static Field bestMove(Field state, Player currentPlayer) {
        Field[] moves = MoveDetector.allMoves(state, currentPlayer);
        Arrays.sort(moves, (m1, m2) -> Double.compare(score(m2, currentPlayer), score(m1, currentPlayer)));
        
        if (moves.length == 0) return state;
        if (moves.length == 1) return moves[0];
        
        if (RND.nextBoolean() && score(moves[0], currentPlayer) == score(moves[1], currentPlayer)) {
            return moves[1];
        }
        
        return  moves[0];
    }
}
