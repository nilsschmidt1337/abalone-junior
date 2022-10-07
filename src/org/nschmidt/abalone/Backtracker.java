package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;

public enum Backtracker {
    INSTANCE;
    
    public static long backtrack(long state, long player, int depth) {
        long[] moves = allMoves(state, player);
        long maxScore = Long.MIN_VALUE;
        long maxMove = moves[0];
        
        final long opponent = 1 + player % 2;
        for (long move : moves) {
            long initialScore = score(move, player);
            long result = playRound(move, opponent, depth);
            long score = score(result, player);
            if (score > maxScore && initialScore > maxScore) {
                maxScore = initialScore;
                maxMove = move;
            }
        }
        
        return maxMove;
    }
    
    
    private static long playRound(long state, long currentPlayer, int maxDepth) {
        int depth = 0;
        while (depth < maxDepth) {
            long[] moves = allMoves(state, currentPlayer);
            
            long maxScore = Long.MIN_VALUE;
            long maxMove = moves[0];
            
            for (long move : moves) {
                long score = score(move, currentPlayer);
                if (maxScore == Long.MAX_VALUE) break;
                if (score > maxScore) {
                    maxScore = score;
                    maxMove = move;
                }
            }
            
            state = maxMove;
            
            currentPlayer = 1 + currentPlayer % 2;
            if (wins(state, currentPlayer)) break;
            depth++;
        }
        
        return state;
    }
}
