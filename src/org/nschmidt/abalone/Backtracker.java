package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;

public enum Backtracker {
    INSTANCE;
    
    public static Field backtrack(Field state, Player player, int depth) {
        Field[] moves = allMoves(state, player);
        long maxScore = Long.MIN_VALUE;
        Field maxMove = moves[0];
        
        if (score(state, player) == Long.MIN_VALUE) {
            for (Field move : moves) {
                long score = score(move, player);
                if (score > maxScore) {
                    maxScore = score;
                    maxMove = move;
                }
            }
            
            return maxMove;
        }
        
        final Player opponent = player.switchPlayer();
        for (Field move : moves) {
            long initialScore = score(move, player);
            Field result = playRound(move, opponent, depth);
            long score = score(result, player);
            if (score > maxScore && initialScore > maxScore) {
                maxScore = initialScore;
                maxMove = move;
            }
        }
        
        return maxMove;
    }
    
    
    private static Field playRound(Field state, Player currentPlayer, int maxDepth) {
        int depth = 0;
        while (depth < maxDepth) {
            Field[] moves = allMoves(state, currentPlayer);
            
            long maxScore = Long.MIN_VALUE;
            Field maxMove = moves[0];
            
            for (Field move : moves) {
                long score = score(move, currentPlayer);
                if (score > maxScore) {
                    maxScore = score;
                    maxMove = move;
                }
            }
            
            state = maxMove;
            
            currentPlayer = currentPlayer.switchPlayer();
            if (wins(state, currentPlayer)) break;
            depth++;
        }
        
        return state;
    }
}
