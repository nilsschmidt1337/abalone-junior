package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;

public enum Backtracker {
    INSTANCE;
    
    public static long backtrack(long state, Player player, int depth) {
        long[] moves = allMoves(state, player);
        long maxScore = Long.MIN_VALUE;
        long maxMove = moves[0];
        
        final Player opponent = player.switchPlayer();
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
    
    
    private static long playRound(long state, Player currentPlayer, int maxDepth) {
        int depth = 0;
        while (depth < maxDepth) {
            long[] moves = allMoves(state, currentPlayer);
            
            long maxScore = Long.MIN_VALUE;
            long maxMove = moves[0];
            
            for (long move : moves) {
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
